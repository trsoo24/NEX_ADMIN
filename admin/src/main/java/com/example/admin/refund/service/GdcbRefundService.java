package com.example.admin.refund.service;

import com.example.admin.common.service.gdcb.util.GdcbSshClient;
import com.example.admin.common.service.gdcb.util.GoogleFtpclient;
import com.example.admin.common.service.gdcb.util.LogUtil;
import com.example.admin.refund.dto.*;
import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.type.BillingState;
import com.example.admin.refund.dto.type.JobType;
import com.example.admin.refund.mapper.RefundMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GdcbRefundService {
    private final RefundMapper refundMapper;
    private final GdcbSshClient gdcbClient;
    private final GoogleFtpclient googleClient;
    private final BillingProcessService billingProcessService;
    private final LogUtil logUtil;
    @Value("${LGUDCB.BILLING.TLO}")
    private String billingTlo;
    @Value("${Billing.Work.Path}")
    private String localWorkPath;
    @Value("${LGUDCB.BILLING.CSV}")
    private String billingCsv;
    @Value("${FTP.Google.Upload.Path}")
    private String googleUploadPath;

    public List<RefundDto> getRefundDtoList(String correlationId) throws Exception {
        List<RefundJob> refundJobList = findCorrelationID(correlationId);
        List<RefundDto> responseList = new ArrayList<>();

        for (RefundJob refundJob : refundJobList) {
            AuthInfo authInfo = refundJob.getAuth();

            RefundDto refundDto = RefundDto.toRefundDto(authInfo);
            responseList.add(refundDto);
        }

        return responseList;
    }

    public boolean refundProcess(HttpServletRequest request, RefundProcessDto refundProcessDto) {
        logUtil.umkInfoLogging(request,"processRefund start~!", "");

        try {
            String ctn =  refundProcessDto.getCtn();
            String correlationId = refundProcessDto.getCorrelationId();
            log.info("환불작업 시작 : {}, {}", ctn, correlationId);

            if (correlationId != null && !refundProcessDto.getCorrelationId().isEmpty()) {

                // 구매이력 조회
                List<RefundJob> CorrelationIdList = findCorrelationID(correlationId);

                AuthInfo authInfo = CorrelationIdList.get(0).getAuth();

                // 과금파일 조회 (CSV.PGP)
                RefundJob findCsvFile = findCsvFile(CorrelationIdList.get(0));

                if (findCsvFile != null) {
                    log.debug("과금파일경로: {}", findCsvFile.getPath());
                    log.debug("과금 요청파일: {}", findCsvFile.getRequestFile());
                    log.debug("과금 결과파일: {}", findCsvFile.getResponseFile());

                    // 빌링파일 다운로드(LGU+)
                    downloadPGPFiles(findCsvFile);

                    // 환불 재처리
                    billingProcessService.billingProcess(findCsvFile);

                    // FTP 업로드
                    log.info("Google FTP Upload: {}, {}",findCsvFile.getPath() , findCsvFile.getResponseFile());

                    uploadGoogleFTP(findCsvFile.getPath(), findCsvFile.getResponseFile());

                    // 처리 결과 view 업데이트
                    updateRefundAuth("GDCB", correlationId);

                    return true;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return false;
        }
        logUtil.umkInfoLogging(request, "processRefund end~!", "");

        return true;
    }


    public List<RefundJob> findCorrelationID(String correlationId) throws Exception {
        RefundJob refundJob;
        AuthInfo authInfo;
        List<RefundJob> result = new ArrayList<>();

        ManualRefund manualRefund = refundMapper.selectManualRefundByCorrelationId(correlationId);

        if(manualRefund != null &&  BillingState.REFUND.getBillingState().equals(manualRefund.getTransactionType())) {
            refundJob = new RefundJob(JobType.DB_Type);

            authInfo = AuthInfo.toAuthInfo(manualRefund);

            refundJob.setAuth(authInfo);

            log.info("이미 재처리된 구매건: {}, {}", authInfo.getCorrelationId(), authInfo.getTransactionDt());
            result.add(refundJob);

            return result;
        }

        // DB 구매 이력 검색
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("billingAgreement", "LGU_DCB");
        requestMap.put("correlationID", correlationId);

        authInfo = refundMapper.getAuthInfo(requestMap);


        if (authInfo != null && authInfo.getCorrelationId() != null) {
            refundJob = new RefundJob(JobType.DB_Type);
            refundJob.setAuth(authInfo);

            log.info("DB 구매건 존재: {}, {}", authInfo.getCorrelationId(), authInfo.getTransactionDt());

            result.add(refundJob);

            return result;
        }

        // 파일 시스템 검색
        try {

            log.debug("TLO 로그 검색 시작- {}", correlationId);

            if (gdcbClient == null || !gdcbClient.isConnected()) {
                initalizeGDCB();
            }

            List<String> tloCmd = makeTloCmd(correlationId);

            List<String> tloResult = gdcbClient.executeCommand(tloCmd);

            if (tloResult == null || tloResult.isEmpty()) {
                log.info("TLO 로그 검색 결과 없음");
            } else {
                log.debug("TLO 로그 검색 결과: {}", tloResult.size());
                log.debug("TLO 로그 검색 결과: {}", tloResult.get(tloResult.size() - 1));

                result = new ArrayList<>();

                for (String tlo : tloResult) {
                    if(!tlo.endsWith(".log")) {
                        authInfo = convertorAuthDomain(tlo);

                        refundJob = new RefundJob(JobType.File_Type);
                        refundJob.setAuth(authInfo);

                        result.add(refundJob);
                    }
                }

            }

        } catch (Exception ex) {
            log.error(ex.getMessage());

            throw ex;
        } finally {
            // 원격서버 연결 종료
            gdcbClient.disConnect();
        }

        return result;
    }


    private ManualRefund findManualRefundByCorrelationId(String correlationId) {
        return refundMapper.selectManualRefundByCorrelationId(correlationId);
    }

    // TB_MANUAL_REFUND 테이블 TransactionType : B 에서 R 로 변경
    public void updateRefundAuth(String dcb, String correlationId) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("correlationId", correlationId);
        requestMap.put("transactionType", "R");

        refundMapper.updateRefundAuth(requestMap);
    }

    /**
     * LG U+ GDCB 서버 접속 정보를 초기화 한다.
     * @throws Exception
     */
    private void initalizeGDCB() throws Exception {

        if (gdcbClient == null || !gdcbClient.isConnected()) {

            log.debug("GDCB 서버 접속 초기화...");

            boolean connect = gdcbClient.connect();

            if (!connect) {
                log.error("원격서버 접속 실패!!!");

                if (!gdcbClient.connect()) {
                    log.error("원격서버 재시도 접속 실패!!!");
                }
            }
        }
    }

    /**
     *  현재 월까지 작업 경로에 포함한다.
     */
    private List<String> makeTloCmd(String correlationID) {
        List<String> list = new ArrayList<String>();

        int startYear = 2013;
        int startMonth = 3;

        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        endMonth++;

        while (startYear <= endYear) {

            while (startMonth <= 12) {

                String sb = "grep" +
                        " '" +
                        correlationID +
                        "' " +
                        billingTlo +
                        "/" +
                        (startYear * 100 + startMonth) +
                        "*/GDCB*.log" +
                        "\n";

                list.add(sb);

                startMonth++;

                if (startMonth > 12) {
                    startMonth = 1;
                    break;
                } else if (startYear == endYear && startMonth > endMonth) {
                    break;
                }
            }
            startYear++;
        }
        log.debug("cmd size: {}", list.size());

        return list;
    }

    private AuthInfo convertorAuthDomain(String tlo) {

        log.info("TLO 결과: {}", tlo);

        String[] authString = tlo.split("\\|");
        if (authString[0].indexOf("=") <= 0) {
            String[] split = authString[0].split(":");
            log.debug("TLO 변환: {}, {}", authString[0], split.length);
        }

        AuthInfo auth = AuthInfo.tloToAuthInfo(tlo);


        log.debug("트랜젝션 날짜 {}", auth.getTransactionDt());

        return auth;
    }

    public RefundJob findCsvFile(RefundJob refundJob) throws Exception {
        /*
         * 과금요청 파일이 복수 개일때 마지막 요청 건을 우선순위로 함.
         */
        try {

            if (refundJob == null || refundJob.getAuth() == null) {
                return refundJob;
            }

            initalizeGDCB();

            List<String> csvCmd = makeCSVCmd(refundJob.getAuth().getCorrelationId());

            List<String> result = gdcbClient.executeCommand(csvCmd);

            log.debug("CSV 결과: {}", result.size());

            if (result != null && result.size() > 0) {
                String[] strings = this.splitResult(result.get(result.size() - 1));

                String path = strings[0].substring(0, strings[0].lastIndexOf("/"));
                String file = strings[0].substring(strings[0].lastIndexOf("/") + 1);

                if(!file.endsWith(".pgp")) {
                    file = file + ".pgp";
                }
                refundJob.setPath(path);
                refundJob.setRequestFile(file.replaceAll("response_", "request_"));
                refundJob.setResponseFile(file);
                refundJob.setBilling(strings[1]);
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            throw new Exception("과금요청 파일을 찾을 수 없습니다.");
        } finally {
            // 원격서버 연결 종료
            gdcbClient.disConnect();
        }
        return refundJob;
    }

    public boolean downloadPGPFiles(RefundJob refundJob) throws Exception {
        initalizeGDCB();

        gdcbClient.fileDownload(refundJob.getPath(), localWorkPath, refundJob.getRequestFile());

        refundJob.setPath(localWorkPath);
        return true;
    }

    public boolean uploadGoogleFTP(String path, String file) throws Exception {
        this.initalizeGoogleFTP();

        googleClient.upload(path + "/" + file, googleUploadPath + "/" + file);

        return true;
    }

    private List<String> makeCSVCmd(String correlationID) {
        List<String> list = new ArrayList<String>();

        int startYear = 2013;
        int startMonth = 3;

        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        endMonth++;

        while (startYear <= endYear) {

            while (startMonth <= 12) {
                StringBuilder sb = new StringBuilder();

                sb.append("grep");
                sb.append(" '");
                sb.append(correlationID);
                sb.append("' ");
                sb.append(billingCsv);
                sb.append("/");

                if (startYear >= 2015) {
                    sb.append(startYear);
                    sb.append("/");
                    if (startMonth < 10) {
                        sb.append("0");
                    }
                    sb.append(startMonth);
                    sb.append("/*/response*.csv");
                } else {
                    sb.append(startYear * 100 + startMonth);
                    sb.append("/*/response*.csv");
                }
                sb.append("\n");

                list.add(sb.toString());

                startMonth++;

                if (startMonth > 12) {
                    startMonth = 1;
                    break;
                } else if (startYear == endYear && startMonth > endMonth) {
                    break;
                }
            }

            startYear++;
        }

        log.debug("cmd size: {}", list.size());

        return list;
    }

    private String[] splitResult(String cmd) {

        int indexOf = cmd.indexOf(":");

        if (indexOf > 0) {
            String fileName = cmd.substring(0, indexOf);
            String tlo = cmd.substring(indexOf + 1);

            log.debug("file name is '{}'", fileName);
            log.debug("TLO = {}", tlo);

            String[] result = new String[2];
            result[0] = fileName;
            result[1] = tlo;

            return result;
        }

        return null;
    }

    private void initalizeGoogleFTP() throws Exception {

        if (googleClient == null || !googleClient.isConnected()) {

            log.debug("Google FTP 서버 접속 초기화...");

            boolean connect = googleClient.connect();

            if (!connect) {
                log.error("Google 서버 접속 실패!!!");

                if (!googleClient.connect()) {
                    log.error("Google 서버 재시도 접속 실패!!!");
                }
            }
        }

    }

    public void insertGdcbAuthInfo(AuthInfo authInfo) {
        refundMapper.insertGdcbAuthInfo(authInfo);
    }

    public void insertManualRefund(ManualRefund manualRefund) {
        refundMapper.insertManualRefund(manualRefund);
    }

    public void insertManualRefundFileInfo(ManualRefundFileInfo manualRefundFileInfo) {
        refundMapper.insertManualRefundFileInfo(manualRefundFileInfo);
    }
}
