package com.example.admin.sdcb.billing_history.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.sdcb.billing_history.dto.ErrorBillingHistory;
import com.example.admin.sdcb.billing_history.dto.BillingHistory;
import com.example.admin.sdcb.billing_history.mapper.BillingHistoryMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SdcbBillingHistoryService {
    private final BillingHistoryMapper billingHistoryMapper;
    private final FunctionUtil functionUtil;
    private final String[] errorBillingHistoryColumnArray = {"CASE", "요청 ID", "TRANSACTION ID", "결제 유형", "결제 금액", "처리 시간"};

    public void insertBillingHistory(BillingHistory billingHistory) {
        billingHistoryMapper.insertBillingHistory(billingHistory);
    }


    public Page<ErrorBillingHistory> errorBillingHistoryToPage(String dcb, String startDate, String endDate, String caseCode, String ctn, int page, int pageSize) {
        List<BillingHistory> billingHistoryList = getBillingHistoryList(dcb, startDate, endDate, caseCode, ctn);
        List<ErrorBillingHistory> errorBillingHistoryList = toErrorBillingHistoryList(billingHistoryList);

        return functionUtil.toPage(errorBillingHistoryList, page, pageSize);
    }

    private List<ErrorBillingHistory> toErrorBillingHistoryList(List<BillingHistory> billingHistoryList) {
        List<ErrorBillingHistory> errorBillingHistoryList = new ArrayList<>();

        for (BillingHistory billingHistory : billingHistoryList) {
            ErrorBillingHistory errorBillingHistory = ErrorBillingHistory.of(billingHistory);
            errorBillingHistoryList.add(errorBillingHistory);
        }

        return errorBillingHistoryList;
    }

    private List<BillingHistory> getBillingHistoryList(String dcb, String startDate, String endDate, String caseCode, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("caseCode", caseCode);
        requestMap.put("ctn", ctn);

        return billingHistoryMapper.selectBillingHistoryList(requestMap);
    }

    public void exportErrorBillingHistoryExcel(String dcb, String startDate, String endDate, String caseCode, String ctn, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("SDCB 대사 오류 이력 조회");

        // 오른쪽 정렬
        CellStyle rightAlignStyle = workbook.createCellStyle();
        rightAlignStyle.setAlignment(HorizontalAlignment.RIGHT);

        // 중앙 정렬
        CellStyle centerAlignStyle = workbook.createCellStyle();
        centerAlignStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = 0;
        XSSFRow row = sheet.createRow(rowNum++);
        for (int i = 0; i < errorBillingHistoryColumnArray.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(errorBillingHistoryColumnArray[i]);
            cell.setCellStyle(centerAlignStyle);
        }

        List<BillingHistory> billingHistoryList = getBillingHistoryList(dcb, startDate, endDate, caseCode, ctn);
        List<ErrorBillingHistory> errorBillingHistoryList = toErrorBillingHistoryList(billingHistoryList);

        for (ErrorBillingHistory errorBillingHistory : errorBillingHistoryList) {
            XSSFRow rowIdx = sheet.createRow(rowNum++);
            rowIdx.setRowStyle(centerAlignStyle);
            rowIdx.createCell(0).setCellValue(errorBillingHistory.getCaseCode());
            rowIdx.createCell(1).setCellValue(errorBillingHistory.getRequestId());
            rowIdx.createCell(2).setCellValue(errorBillingHistory.getTransactionId());
            rowIdx.createCell(3).setCellValue(errorBillingHistory.getStatusCode());
            rowIdx.createCell(5).setCellValue(errorBillingHistory.getEventTime());

            // 가격만 오른쪽 정렬
            XSSFCell cell = rowIdx.createCell(4);
            cell.setCellStyle(rightAlignStyle);
            cell.setCellValue(errorBillingHistory.getTotAmt());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=SDCBErrorBillingHistory.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
