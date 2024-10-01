package com.example.admin.service.reconcile.gdcb;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.reconcile.gdcb.GDCBMonthlyInvoiceSum;
import com.example.admin.repository.mapper.reconcile.gdcb.ReconcileMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GDCBInvoiceDetailService {
    private final ReconcileMapper reconcileMapper;
    private final FunctionUtil functionUtil;

    private final String[] revsCategoryArray = {"APP", "APP_SUBSCRIPTION", "CONTENT", "NA", "SPECIAL_APP"};

    public Map<String, Map<String, List<GDCBMonthlyInvoiceSum>>> getGDCBInvoiceDetailMap(String dcb, String month) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] monthArray = month.split("-");
        String previousMonth = calculatePreviousDate(month);
        String[] preMonthArray = previousMonth.split("-");
        Map<String, Map<String, List<GDCBMonthlyInvoiceSum>>> responseMap = new LinkedHashMap<>();
        requestMap.put("dcb", dcb);

        // 지난 달 값
        requestMap.put("year", preMonthArray[0]);
        requestMap.put("month", preMonthArray[1]);
        responseMap.put(previousMonth + " Invoice Details(DCB + 소액결제 + 기타)", getDetails("전체", requestMap));
        responseMap.put(previousMonth + " Invoice Details(DCB)", getDetails("00", requestMap));
        responseMap.put(previousMonth + " Invoice Details(소액결제)", getDetails("PG", requestMap));
        responseMap.put(previousMonth + " Invoice Details(기타)", getDetails("99", requestMap));

        // 이달 값
        requestMap.put("year", monthArray[0]);
        requestMap.put("month", monthArray[1]);

        responseMap.put(month + " Invoice Details(DCB + 소액결제 + 기타)", getDetails("전체", requestMap));
        responseMap.put(month + " Invoice Details(DCB)", getDetails("00", requestMap));
        responseMap.put(month + " Invoice Details(소액결제)", getDetails("PG", requestMap));
        responseMap.put(month + " Invoice Details(기타)", getDetails("99", requestMap));

        return responseMap;
    }

    private Map<String, List<GDCBMonthlyInvoiceSum>> getDetails(String paymentType, Map<String, Object> requestMap) {
        Map<String, List<GDCBMonthlyInvoiceSum>> responseMap = new LinkedHashMap<>();
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceBuySumList;
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceRefundSumList;

        if(!paymentType.equals("전체")) {
            requestMap.put("paymentType", paymentType);
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);

        } else {
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);
        }

        addMap(responseMap, monthlyInvoiceBuySumList, monthlyInvoiceRefundSumList);

        return responseMap;
    }

    private void addMap(Map<String, List<GDCBMonthlyInvoiceSum>> map, List<GDCBMonthlyInvoiceSum> buyList, List<GDCBMonthlyInvoiceSum> refundList) {
        setListSize(buyList);
        setListSize(refundList);
        List<GDCBMonthlyInvoiceSum> totalList = new ArrayList<>();

        for (int i = 0; i < buyList.size(); i++) {
            buyList.get(i).calculateChargeSum();
            refundList.get(i).calculateChargeSum();

            GDCBMonthlyInvoiceSum totalSum = GDCBMonthlyInvoiceSum.toTransactionTypeTotal(buyList.get(i), refundList.get(i));
            totalList.add(totalSum);
        }
        addRevsCategoryTotal(totalList);
        addRevsCategoryTotal(buyList);
        addRevsCategoryTotal(refundList);

        map.put("SUM", totalList);
        map.put("CHARGE", buyList);
        map.put("REFUND", refundList);
    }

    private void setListSize(List<GDCBMonthlyInvoiceSum> sumList) {
        // 비어있는 revsCategory 값 채워서 buy, refund list 길이 통일
        Boolean[] revsCategoryCheck = new Boolean[revsCategoryArray.length];

        for (GDCBMonthlyInvoiceSum sum : sumList) {
            for (int i = 0; i < revsCategoryArray.length; i++) {
                if (sum.getRevsCategory().equals(revsCategoryArray[i])) {
                    revsCategoryCheck[i] = true;
                }
            }
        }

        for (int j = 0; j < revsCategoryCheck.length; j++) {
            if (!revsCategoryCheck[j]) {
                GDCBMonthlyInvoiceSum defaultInvoiceSum = GDCBMonthlyInvoiceSum.generateDefault(sumList.get(0), revsCategoryArray[j]);
                sumList.add(defaultInvoiceSum);
            }
        }

        sumList.sort(Comparator.comparing(GDCBMonthlyInvoiceSum::getRevsCategory));
    }

    private void addRevsCategoryTotal(List<GDCBMonthlyInvoiceSum> sumList) {
        GDCBMonthlyInvoiceSum revsCategoryTotal = GDCBMonthlyInvoiceSum.toRevsCategoryTotal(sumList.get(0));

        for (GDCBMonthlyInvoiceSum sum : sumList) {
            revsCategoryTotal.addTransactionCnt(sum.getTransactionCnt());
            revsCategoryTotal.addAmountSum(sum.getTotalAmountSum());
            revsCategoryTotal.addCurrencySum(sum.getRevsInInvoicedCurrencySum());
        }
        revsCategoryTotal.calculateChargeSum();

        sumList.add(0, revsCategoryTotal);
    }

    public void exportInvoiceDetailExcel(String dcb, String month, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("일별 통계");

        // 왼쪽 정렬
        CellStyle leftAlignStyle = workbook.createCellStyle();
        leftAlignStyle.setAlignment(HorizontalAlignment.LEFT);
        // 중앙 정렬
        CellStyle centerAlignStyle = workbook.createCellStyle();
        centerAlignStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = 0;
        XSSFRow row = sheet.createRow(rowNum++);
        String previousDate = calculatePreviousDate(month);
        XSSFRow subRow = sheet.createRow(rowNum++);
        String[] subHeaders = {"결제 건수(건)", "거래 금액(원)", "U+ 매출(원)", "수수료(%)"};

        for (int i = 0; i < 9; i++) {
            XSSFCell cell = row.createCell(i);
            XSSFCell subCell = subRow.createCell(i);
            int subHeaderIdx = (i - 1) % subHeaders.length;

            if (i == 0) {
                cell.setCellValue("거래 유형");
                cell.setCellStyle(leftAlignStyle);
            } else if (i < 5) {
                cell.setCellValue(previousDate);
                subCell.setCellValue(subHeaders[subHeaderIdx]);
                cell.setCellStyle(centerAlignStyle);
            } else {
                cell.setCellValue(month);
                subCell.setCellValue(subHeaders[subHeaderIdx]);
                cell.setCellStyle(centerAlignStyle);
            }
        }

        // "거래 유형" 병합
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        // 날짜 값 병합
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 8));

        Map<String, Map<String, List<GDCBMonthlyInvoiceSum>>> invoiceMap = getGDCBInvoiceDetailMap(dcb, month);

        for (Map<String, List<GDCBMonthlyInvoiceSum>> mapIdx : invoiceMap.values()) {
            for(List<GDCBMonthlyInvoiceSum> list : mapIdx.values()) {
                XSSFRow idxRow = sheet.createRow(rowNum++);
                for(GDCBMonthlyInvoiceSum sum : list) {
                    idxRow.createCell(0).setCellValue(sum.getRevsCategory());
                    idxRow.createCell(1).setCellValue(sum.getTransactionCnt());
                    idxRow.createCell(2).setCellValue(sum.getTotalAmountSum());
                    idxRow.createCell(3).setCellValue(sum.getRevsInInvoicedCurrencySum());
                    idxRow.createCell(4).setCellValue(sum.getChargeSum());
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=GDCBInvoiceDetail.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private String calculatePreviousDate(String month) {
        int monthInt = Integer.parseInt(month);
        String year = month.substring(0, month.indexOf("-"));

        if (monthInt == 1) { // 1월이면 전년 12월
            int prevYear = Integer.parseInt(year) - 1;
            return prevYear + "-12";
        } else {
            monthInt -= 1;
            return year + "-" + String.format("%02d", monthInt);
        }
    }
}
