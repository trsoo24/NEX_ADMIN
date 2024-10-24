package com.example.admin.reconcile.service;

import com.example.admin.reconcile.dto.GDCBDetailCompare;
import com.example.admin.reconcile.dto.GDCBMonthlyInvoiceSum;
import com.example.admin.reconcile.dto.GoogleMonthlyInvoiceSum;
import com.example.admin.reconcile.dto.MonthlyInvoiceSum;
import com.example.admin.reconcile.mapper.ReconcileMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GDCBInvoiceDetailService {
    private final ReconcileMapper reconcileMapper;

    private final String[] revsCategoryArray = {"APP", "APP_SUBSCRIPTION", "CONTENT", "NA", "SPECIAL_APP"};
    private final String[] paymentTypeArray = {"Invoice Details(DCB + 소액결제 + 기타)", "Invoice Details(DCB)", "Invoice Details(소액결제)", "Invoice Details(기타)"};

    public Map<String, List<GDCBDetailCompare>> getGDCBInvoiceDetailMap(String dcb, String month) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] monthArray = month.split("-");
        String previousMonth = calculatePreviousDate(month);
        String[] preMonthArray = previousMonth.split("-");
        List<GDCBDetailCompare> responseList = new ArrayList<>();
        Map<String, List<GDCBDetailCompare>> responseMap = new LinkedHashMap<>();

        requestMap.put("dcb", dcb);

        // 지난 달 값
        requestMap.put("year", preMonthArray[0]);
        requestMap.put("month", preMonthArray[1]);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(previousMonth, paymentTypeArray[0], getDetails("전체", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(previousMonth, paymentTypeArray[1], getDetails("00", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(previousMonth, paymentTypeArray[2], getDetails("PG", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(previousMonth, paymentTypeArray[3], getDetails("99", requestMap), responseList);

        // 이번 달 값
        requestMap.put("year", monthArray[0]);
        requestMap.put("month", monthArray[1]);

        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[0], getDetails("전체", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[1], getDetails("00", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[2], getDetails("PG", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[3], getDetails("99", requestMap), responseList);

        responseMap.put("invoiceDetailsFileContents", responseList);
        // Google Summary File
        List<GoogleMonthlyInvoiceSum> googleMonthlySumList = getGoogleMonthlySum(requestMap);
        googleMonthlySumToGDCBCompareDtoList(month, googleMonthlySumList, responseMap);

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
        boolean[] revsCategoryCheck = new boolean[revsCategoryArray.length];

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

    private void gdcbMonthlyInvoiceSumToGDCBCompareDtoList(String month, String parameterType, Map<String, List<GDCBMonthlyInvoiceSum>> sumMap, List<GDCBDetailCompare> responseList) { // 객체 하나로 처리
        for (List<GDCBMonthlyInvoiceSum> list : sumMap.values()) {
            for (GDCBMonthlyInvoiceSum sum : list) {
                responseList.add(GDCBDetailCompare.fromTbMonthlyInvoiceSum(month, parameterType, sum));
            }
        }
    }

    private void googleMonthlySumToGDCBCompareDtoList(String month, List<GoogleMonthlyInvoiceSum> googleList, Map<String, List<GDCBDetailCompare>> responseMap) {
        List<GDCBDetailCompare> responseList = new ArrayList<>();
        for (GoogleMonthlyInvoiceSum sum : googleList) {
            responseList.add(GDCBDetailCompare.fromGMonthlyInvoiceSum(month, sum));
        }

        responseMap.put("summaryFileContents", responseList);
    }

    private List<GoogleMonthlyInvoiceSum> getGoogleMonthlySum(Map<String, Object> requestMap) {
        List<GoogleMonthlyInvoiceSum> sumList = reconcileMapper.selectGoogleSummary(requestMap);
        GoogleMonthlyInvoiceSum total = GoogleMonthlyInvoiceSum.toRevsCategoryTotal(sumList.get(0).getYear(), sumList.get(0).getMonth());

        for (GoogleMonthlyInvoiceSum sum : sumList) {
            total.addItemPriceSum(sum.getItemPriceSum());
            total.addTaxSum(sum.getTaxSum());
            total.addTotalAmountSum(sum.getTotalAmountSum());
            total.addRevShareSum(sum.getRevShareSum());
        }
        sumList.add(sumList.size() - 1, total);

        return sumList;
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

        Map<String, List<GDCBDetailCompare>> detailMap = getGDCBInvoiceDetailMap(dcb, month);
        List<GDCBDetailCompare> gdcbDetailCompareList = detailMap.get("invoiceDetailsFileContents");

        for (GDCBDetailCompare compare : gdcbDetailCompareList) {
            XSSFRow idxRow = sheet.createRow(rowNum++);
            if (compare.getTYear() != null && compare.getGYear() == null) { // Monthly Invoice
                idxRow.createCell(0).setCellValue(compare.getRevsCategory());
                idxRow.createCell(1).setCellValue(compare.getTransactionCnt());
                idxRow.createCell(2).setCellValue(compare.getTotalAmountSum());
                idxRow.createCell(3).setCellValue(compare.getRevsInInvoicedCurrencySum());
                idxRow.createCell(4).setCellValue(compare.getChargeSum());
            }
        }

        // Google Summary File
        XSSFRow googleSummaryRow = sheet.createRow(rowNum);
        googleSummaryRow.createCell(0).setCellValue("Google Summary File");
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum++,0,4));

        XSSFRow googleSummaryCol = sheet.createRow(rowNum++);
        googleSummaryCol.createCell(0).setCellValue("아이템 구분");
        googleSummaryCol.createCell(1).setCellValue("ItemPriceSum");
        googleSummaryCol.createCell(2).setCellValue("TaxSum");
        googleSummaryCol.createCell(3).setCellValue("TotalAmountSum");
        googleSummaryCol.createCell(4).setCellValue("RevShareSum");

        List<GDCBDetailCompare> googleDetailList = detailMap.get("summaryFileContents");

        for (GDCBDetailCompare compare : googleDetailList) {
            XSSFRow idxRow = sheet.createRow(rowNum++);
            if (compare.getGYear() != null && compare.getTYear() == null) { // Google Invoice
                idxRow.createCell(0).setCellValue(compare.getRevsCategory());
                idxRow.createCell(1).setCellValue(compare.getItemPriceSum());
                idxRow.createCell(2).setCellValue(compare.getTaxSum());
                idxRow.createCell(3).setCellValue(compare.getTotalAmountSum());
                idxRow.createCell(4).setCellValue(compare.getRevShareSum());
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
        int monthInt = Integer.parseInt(month.substring(month.indexOf("-") + 1));
        String year = month.substring(0, month.indexOf("-"));

        if (monthInt == 1) { // 1월이면 전년 12월
            int prevYear = Integer.parseInt(year) - 1;
            return prevYear + "-12";
        } else {
            monthInt -= 1;
            return year + "-" + String.format("%02d", monthInt);
        }
    }

    @Transactional
    public void insertInvoiceDetailData(String year) {
        String month;
        Random random = new Random();
        String[] transactionTypes = {"B", "R"};
        String[] paymentTypes = {"00", "99", "PG"};


        for (int i = 1; i <= 12; i++) {
            month = String.valueOf(i);
            if (i < 10) {
                month = "0" + i;
            }

            for (int j = 0; j < transactionTypes.length; j++) {
                String transactionType = transactionTypes[j];
                for (int k = 0; k < paymentTypes.length; k++) {
                    String paymentType = paymentTypes[k];
                    for (int l = 0; l < revsCategoryArray.length; l++) {
                        String revsCategory = revsCategoryArray[l];
                        double cnt = Math.floor(random.nextDouble(1000));
                        double itemPriceSum = Math.floor(random.nextDouble(100000));
                        double taxSum = Math.floor(random.nextDouble(10000));
                        double totalAmountSum = Math.floor(random.nextDouble(1000000));
                        double revsInInvoicedSum = Math.floor(random.nextDouble(1000000));
                        MonthlyInvoiceSum monthlyInvoiceSum = MonthlyInvoiceSum.toEntity(year, month, transactionType, paymentType, revsCategory,
                                cnt, itemPriceSum, taxSum, totalAmountSum, revsInInvoicedSum);
                        reconcileMapper.insertGDCBMonthlyInvoice(monthlyInvoiceSum);
                    }
                }
            }

            for (int l = 0; l < revsCategoryArray.length; l++) {
                String revsCategory = revsCategoryArray[l];
                double itemPriceSum = Math.floor(random.nextDouble(100000));
                double taxSum = Math.floor(random.nextDouble(10000));
                double totalAmountSum = Math.floor(random.nextDouble(1000000));
                double revShareSum = Math.floor(random.nextDouble(10000));

                GoogleMonthlyInvoiceSum googleInvoiceSum = GoogleMonthlyInvoiceSum.toEntity(year, month, revsCategory, itemPriceSum, taxSum, totalAmountSum, revShareSum);
                reconcileMapper.insertGoogleMonthlyInvoice(googleInvoiceSum);
            }
        }
    }
}
