package com.example.admin.refund.service;

import com.example.admin.common.service.gdcb.util.GDCBConstants;
import com.example.admin.common.service.gdcb.util.StringUtil;
import com.example.admin.refund.dto.RefundJob;
import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.type.BillingState;
import com.example.admin.refund.dto.ManualRefund;
import com.example.admin.refund.dto.ManualRefundFileInfo;
import com.example.admin.refund.dto.type.BillingType;
import com.example.admin.refund.dto.vo.Billing;
import com.example.admin.refund.dto.vo.LgudcbEaiSdw;
import com.example.admin.refund.mapper.RefundMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingProcessService {
    private final PGPFileHandler pgpFileHandler;
    private final RefundMapper refundMapper;

    private final SimpleDateFormat formatterMM = new SimpleDateFormat("yyyyMM", Locale.KOREA);
    private final SimpleDateFormat formatterDD = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

    @Transactional(rollbackFor = Exception.class)
    public void billingProcess(RefundJob refundJob) throws Exception {

        log.info("Reqeust CSV: '{}'", refundJob.getRequestFile());
        log.info("Respone CSV: '{}'", refundJob.getResponseFile());
        log.info("CorrelationID: '{}'", refundJob.getAuth().getCorrelationId());

        // 요청파일 PGP 복호화
        pgpFileHandler.decryptePGP(refundJob.getPath() + "/" + refundJob.getRequestFile());

        // 요청 정보 및 CSV 파일 설정
        getResponseFileName(refundJob);

        String requestCSV = getReqCSV(refundJob);
        String responseCSV  = getRespCSV(refundJob);
        AuthInfo reqAuth = refundJob.getAuth();

        File requestFile = new File(requestCSV);

        // 환불정보 조회 및 작업을 위한 등록
        ManualRefund manualRefund = initRefundWork(reqAuth);

        if (requestFile.exists()) {

            log.info("Billing Start file: {}", requestCSV);

            CSVReader csvReader = new CSVReader(new FileReader(requestFile));
            CSVWriter csvWriter = new CSVWriter(new FileWriter(responseCSV), ',', CSVWriter.NO_QUOTE_CHARACTER);

            try {
                // 전체 과금 요청 목록
                List<Billing> sortBilling = new ArrayList<>();

                int count = 0;

                // 파일 레코드수 만큼 루프를 돌면서
                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {

                    // 첫번째 레코드는 REQUESTFILE,1410514176285,295172,LGU_DCB이니까
                    if (count == 0) {
                        File outFile = new File(responseCSV);
                        if (outFile.exists()) {
                            outFile.delete();
                        }
                        Calendar calendar = Calendar.getInstance();
                        Date now = calendar.getTime();
                        Timestamp timeStamp = new Timestamp(now.getTime());
                        long offset = Timestamp.valueOf(timeStamp.toString()).getTime();

                        csvWriter.writeNext(new String[]{"RESPONSEFILE", String.valueOf(offset), nextLine[2], nextLine[3], "", ""});

                        csvWriter.flush();

                        log.info("RESPONSEFILE,{},{}", nextLine[2], nextLine[3]);

                    } else if (BillingType.REFUND.equals(nextLine[0]) && reqAuth.getCorrelationId().equals(nextLine[2])){
                        Billing bill = new Billing();

                        // RESPONSEFILE (CHARGE, CANCLE, REFUND)
                        bill.setType(nextLine[0]);
                        // CSV파일 ORDER CREATION TIME
                        bill.setTimeStamp(nextLine[1]);
                        // Correlation Id(TB_AUTH_INFO)
                        bill.setCorrelationId(nextLine[2]);
                        // Billing Agreement ID(TB_AUTH_INFO)
                        bill.setBillingAgreementId(nextLine[3]);

                        sortBilling.add(bill);
                    }
                    count++;
                }

                // 과금 발생 시간 순서로 정렬
                sortBilling.sort(Comparator.comparing(Billing::getTimeStamp));

                log.info("◇전체 건수: {}, {}", count - 1, sortBilling.size());

                if (sortBilling.isEmpty()) {
                    throw new Exception("환불 요청 정보 없음!");
                }

                log.info("=== CSV write processor start ===");

                for (int i = 0; i < sortBilling.size(); i++) {

                    Billing bill = sortBilling.get(i);

                    log.info("◇ Requset: {}", bill);

                    Billing billing = calculate(bill, manualRefund);

                    csvWriter.writeNext(new String[]{
                            billing.getType(),
                            billing.getTimeStamp(),
                            billing.getCorrelationId(),
                            billing.getBillingAgreementId(),
                            billing.getResultCode(),
                            billing.getMessage()
                    });

                    csvWriter.flush();
                }


                log.info("=== CSV write processor end ===");


            } catch (Exception ex) {
                log.error(ex.getMessage());

                throw ex;
            } finally {
                csvWriter.flush();
                csvReader.close();
                csvWriter.close();
            }

            // 요청파일 PGP 복호화
            pgpFileHandler.encryptePGP(responseCSV);

        } else {
            log.warn("처리할 과금파일이 없습니다.[{}]", requestCSV);

            throw new Exception("처리할 과금파일이 없음!");
        }
    }

    /**
     * 요청에 따른 과금 처리
     */
    private Billing calculate(Billing requestBilling, ManualRefund manualRefund) throws Exception {

        try {
            // 구매예약 정보 없음
            if (manualRefund == null) {

                // BILLING_AGREEMENT : LGU_DCB
                if (requestBilling.getBillingAgreementId().equals(GDCBConstants.SCH_AUTH_BILLING_AGREEMENT)) {
                    // INVALID_CORRELATION_ID
                    requestBilling.setResultCode(GDCBConstants.MESSGE_INVALID_CORRELATION_ID);
                    requestBilling.setMessage("");
                } else {
                    // INVALID_BILLING_AGREEMENT_ID
                    requestBilling.setResultCode(GDCBConstants.MESSGE_INVALID_BILLING_AGREEMENT_ID);
                    requestBilling.setMessage("");
                }

                log.warn("■ 처리불가 과금요청: {}", requestBilling);

                return requestBilling;

            } else {
                // 구매 요청에 따른 처리

                LgudcbEaiSdw eaiSdw = new LgudcbEaiSdw();

                // AUTH 수정
                long longItemPrice = manualRefund.getItemPrice() / 1000000;
                long longTax = manualRefund.getTax() / 1000000;
                long longTotal = manualRefund.getTotal() / 1000000;

                String dbTransactionType = manualRefund.getTransactionType();
                String strUpTranType = null;
                String strAccountType = null;
                String strTransactionPrmDt = null;
                String strTransactionDt = null;
                String strRequestType = null;

                if (isPossible(requestBilling, manualRefund.getTransactionType())) {

                    // 구매 요청
                    if (BillingType.CHARGE.equals(requestBilling.getType())) {
                        strUpTranType = GDCBConstants.SCH_AUTH_TRANSACTION_CHARGE;
                        strAccountType = GDCBConstants.SCH_EAI_ACCOUNT_CHARGE;
                        strTransactionPrmDt = StringUtil.getFormat();
                        strTransactionDt = strTransactionPrmDt;

                        if (manualRefund.getTransactionDt() == null) {
                            manualRefund.setTransactionDt(strTransactionDt);
                        }

                        eaiSdw.setNewPrssYymm(new SimpleDateFormat("yyyyMM").format(new Date()));

                    } else if (BillingType.CANCEL.equals(requestBilling.getType())) {
                        // 구매 취소 요청
                        strUpTranType = GDCBConstants.SCH_AUTH_TRANSACTION_CANCEL;
                        strAccountType = GDCBConstants.SCH_EAI_ACCOUNT_CANCEL;
                        strTransactionPrmDt = StringUtil.getFormat();
                        strTransactionDt = strTransactionPrmDt;

                        if (manualRefund.getTransactionDt() == null) {
                            manualRefund.setTransactionDt(strTransactionDt);
                        }

                        eaiSdw.setNewPrssYymm(new SimpleDateFormat("yyyyMM").format(new Date()));

                    } else if (BillingType.REFUND.equals(requestBilling.getType())) {
                        // 환불 요청
                        strUpTranType = GDCBConstants.SCH_AUTH_TRANSACTION_REFUND;
                        strAccountType = GDCBConstants.SCH_EAI_ACCOUNT_REFUND;
                        strTransactionPrmDt = StringUtil.getFormat();

                        if (manualRefund.getTransactionDt() == null || "null".equals(manualRefund.getTransactionDt())) {
                            manualRefund.setTransactionDt(strTransactionPrmDt);

                            log.info("Tran Date Set: {}, {}", manualRefund.getCorrelationId(), manualRefund.getTransactionDt());
                        } else {
                            log.debug("Tran else: {}, {}", manualRefund.getTransactionDt(), strTransactionPrmDt);
                        }

                        String transactionTimeYYYYMM = manualRefund.getTransactionDt().substring(0, 6);

                        eaiSdw.setNewPrssYymm(transactionTimeYYYYMM);

                        //현재시간기준
                        String currentTimeMM = formatterMM.format(new Date());
                        String currentTimeDD = formatterDD.format(new Date());

                        String beforeTimeMM = StringUtil.addDateFromNow(Calendar.MONTH, -1, "yyyyMM");

                        /*
                         * 이번 달 발생한 구매 건은 환불 가능
                         * 매달 1일에 한하여 지난 달 건도 환불 가능
                         */

                        // 구매 상태 이면 환불 가능
                        if (BillingState.CHARGE.equals(manualRefund.getTransactionType())) {

                            if (currentTimeMM.equals(transactionTimeYYYYMM)) {
                                //환불 승인 & 청구처리
                                strRequestType = GDCBConstants.SCH_EAI_REQUEST_OK;
                            } else if (beforeTimeMM.equals(transactionTimeYYYYMM)) {
                                if ("01".equals(currentTimeDD.substring(6, 8))) {
                                    //환불 승인 & 청구처리
                                    strRequestType = GDCBConstants.SCH_EAI_REQUEST_OK;
                                } else {
                                    //환불 불가 & 청구처리
                                    strRequestType =  GDCBConstants.SCH_EAI_REQUEST_NO;
                                }
                            } else {
                                //환불 불가 & 청구처리
                                strRequestType = GDCBConstants.SCH_EAI_REQUEST_NO;
                            }
                        } else {
                            //환불 불가 & 청구처리
                            strRequestType = GDCBConstants.SCH_EAI_REQUEST_NO;
                        }
                    }

                    /**EAI 추가*/
                    // 배치거래날짜
                    eaiSdw.setNewTransactionDate(strTransactionPrmDt);
                    // 청구처리
                    eaiSdw.setNewRequestType(strRequestType);
                    // 요청에대한고유ID
                    eaiSdw.setNewCorrelationId(manualRefund.getCorrelationId());
                    // 결제유형
                    eaiSdw.setNewAccountType(strAccountType);
                    // 청구서번호
                    eaiSdw.setNewBan(manualRefund.getBan());
                    // 가입계약번호
                    eaiSdw.setNewAceNo(manualRefund.getAceNo());
                    // 상품가격
                    eaiSdw.setNewItemPrice(longItemPrice);
                    // 상품세금
                    eaiSdw.setNewTax(longTax);
                    // 거래금액
                    eaiSdw.setNewTotal(longTotal);
                    // 구입거래시간
                    eaiSdw.setNewAuthDate(manualRefund.getTransactionDt());
                    // 판매자연락처
                    eaiSdw.setNewMerchantContact(manualRefund.getMerchantContact());
                    // 서비스코드
                    eaiSdw.setNewSmlsStlmDvCd(GDCBConstants.SCH_EAI_SMLS_STLM_DV_CD);
                    // 결제대행사서비스코드
                    eaiSdw.setNewSmlsStlmCmpnyCd(GDCBConstants.SCH_EAI_SMLS_STLM_CMPNY_CD);
                    // CTN
                    eaiSdw.setNewProdNo(manualRefund.getCtn());
                    // 판매자
                    eaiSdw.setNewMerchantName(manualRefund.getMerchantName());
                    // 상품명
                    eaiSdw.setNewItemName(manualRefund.getItemName());

                    /*
                     * 기존 중복 처리건에 대해서는 DB Update list에 add 하지 않음.
                     */
                    if (BillingState.CANCEL.equals(dbTransactionType) || BillingState.REFUND.equals(dbTransactionType)) {
                        // TransactionType이 'C', 'R' 로 CANCLE, REFUND 인 경우 DB 작업 Row에 추가 하지 않음.
                        log.info("CANCEL OR REFUND Type 으로 중복 제외 [ Correlationid = {}, TranType = {} ", eaiSdw.getNewCorrelationId(), requestBilling.getType());

                    } else if (BillingState.CHARGE.equals(dbTransactionType)) {

                        if (BillingType.CHARGE.equals(requestBilling.getType()) || BillingType.CANCEL.equals(requestBilling.getType())) {
                            //tranType Google에서 넘어온 값이 CHARGE, CANCEL 일때 CHARGE-CHARGE는 이미 중복 내용, CHARGE-CANCEL 은 올 수 없는 케이스로  DB 작업 Row에 추가 하지 않음.
                            log.info("CANCEL OR CHARGE Type 으로 중복 제외 [ Correlationid = {}, TranType = {} ", eaiSdw.getNewCorrelationId(), requestBilling.getType());
                        } else {
                            log.info("REFUND Type 으로 List 추가 [ Correlationid = {}, TranType = {} ", eaiSdw.getNewCorrelationId(), requestBilling.getType());

                            // 과금 데이타 등록
                            Map<String,Object> requestMap = new HashMap<>();
                            requestMap.put("transactionType", strUpTranType);
                            requestMap.put("correlationId", manualRefund.getCorrelationId());

                            refundMapper.updateRefundAuth(requestMap);

                            // Kafka 로 교체
//                            refundMapper.insertEai(eaiSdw);
                        }

                    } else {
                        log.info("CHARGE Type 으로 처리하지 않음 [ Correlationid = {}, TranType = {} ", eaiSdw.getNewCorrelationId(), requestBilling.getType());
                    }

                    log.info(eaiSdw.toString());

                    requestBilling.setResultCode(GDCBConstants.SUCCESS_RESULT);
                    requestBilling.setMessage("authorizationId" + manualRefund.getAuthTransactionId());
                }
            }

            return requestBilling;

        } catch (Exception ex) {
            log.error(ex.getMessage());

            throw ex;
        }

    }

    /**
     * 재처리 파일 번호 설정
     */
    private void getResponseFileName(RefundJob refundJob) throws Exception {
        ManualRefundFileInfo fileInfo = refundMapper.selectBillingFileInfo(refundJob.getRequestFile());

        // 재처리 파일 번호 설정
        if (fileInfo == null) {
            fileInfo.setResponseName(refundJob.getResponseFile());
            fileInfo.firstTry();

            refundMapper.insertManualRefundFileInfo(fileInfo);

            refundJob.setResponseFile(addNumResponse(refundJob.getResponseFile(), 1));
        } else {
            log.debug("파일정보: {}, {}", fileInfo.getRequestName(), fileInfo.getRetry());

            fileInfo.retry();
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("retry", fileInfo.getRetry());
            requestMap.put("requestName", fileInfo.getRequestName());

            refundMapper.updateRefundFileInfo(requestMap);

            refundJob.setResponseFile(addNumResponse(refundJob.getResponseFile(), fileInfo.getRetry()));
            log.debug("응답파일명: {}", refundJob.getResponseFile());
        }
    }

    /**
     * 요청하는 과금 상태를 처리할 수 있는지 체크.
     */
    private boolean isPossible(Billing bill, String state) {

        // 논리적인 오류 : DCB 규격에 중복 요청인 경우에 처리 방법이 고려되지 않음. 
        log.debug("◇요청 : {}", bill.getCorrelationId());
        log.debug("◇요청 상태: 요청[{}], DB상태[{}]", bill.getType(), state);

        if (BillingType.CHARGE.equals(bill.getType())) {
            if (BillingState.CANCEL.equals(state)) {
                bill.setResultCode("ALREADY_CANCELLED");

                return false;
            }
        } else if (BillingType.CANCEL.equals(bill.getType())) {
            if (BillingState.CHARGE.equals(state)) {
                bill.setResultCode("ALREADY_CHARGED");

                return false;
            }
        } else if (BillingType.REFUND.equals(bill.getType())) {
            if (BillingState.AUTH.equals(state)) {
                bill.setResultCode("NOT_CHARGED");

                return false;
            } else if (BillingState.CANCEL.equals(state)) {
                bill.setResultCode("ALREADY_CANCELLED");

                return false;
            }

            //TODO 정의되지 않은 리턴코드 : ACCOUNT_CLOSED, CHARGE_TOO_OLD

        } else {

            log.error("◇ 처리 불가능한 상태[{}]: 요청[{}], DB상태[{}]", bill.getCorrelationId(), bill.getType());

            return false;
        }

        return true;

    }

    /**
     * 파일명 + 1
     */
    private String addNumResponse(String response, int num){
        String[] split = response.split("\\.");

        StringBuilder sb = new StringBuilder();

        for(int i=0;i<split.length; i++) {
            if(i == 0) {
                sb.append(split[i]);
                sb.append("_");
                sb.append(num);
            } else {
                sb.append(split[i]);
            }

            if(i < split.length - 1) {
                sb.append(".");
            }
        }

        return sb.toString();
    }

    private ManualRefund initRefundWork(AuthInfo reqAuth) {
        ManualRefund manualRefund = ManualRefund.initRefundWork(reqAuth);

        ManualRefund result = refundMapper.selectManualRefundByCorrelationId(manualRefund.getCorrelationId());

        if (result == null) {
            log.debug("환불정보 등록: {}, {}", manualRefund.getCorrelationId(), manualRefund.getTransactionType());

            refundMapper.insertManualRefund(manualRefund);

            return manualRefund;
        }
        return result;
    }

    private String getReqCSV(RefundJob refundJob){

        String sb = refundJob.getPath() +
                "/" +
                refundJob.getRequestFile().replace(".pgp", "");

        return sb;
    }

    private String getRespCSV(RefundJob refundJob) {

        String sb = refundJob.getPath() +
                "/" +
                refundJob.getResponseFile().replace(".pgp", "");

        return sb;
    }
}
