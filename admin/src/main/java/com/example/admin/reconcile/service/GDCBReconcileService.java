package com.example.admin.reconcile.service;

import com.example.admin.reconcile.dto.InsertReconcileDto;
import com.example.admin.reconcile.dto.Reconcile;
import com.example.admin.reconcile.mapper.ReconcileMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GDCBReconcileService {
    private final ReconcileMapper reconcileMapper;

    public List<Reconcile> getGDCBReconcileList(String month, String fileType) {
        month = month.replace("-", ""); // DB 에 '-' 이 없이 저장됨

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("yearMonth", month);
        requestMap.put("fileType", fileType);

        return reconcileMapper.getGDCBReconcileList(requestMap);
    }

    public void exportGDCBExcel(String month, String fileType, HttpServletResponse response) throws IOException {
        List<Reconcile> reconcileList = getGDCBReconcileList(month, fileType);

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("대사 파일 조회");

        int rowNum = 0;
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("구매 년월");
        header.createCell(1).setCellValue("파일 구분");
        header.createCell(2).setCellValue("ID");
        header.createCell(3).setCellValue("파일명");
        header.createCell(4).setCellValue("작업 결과");
        header.createCell(5).setCellValue("일자");

        for (Reconcile reconcile : reconcileList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(reconcile.getYearMonth());
            row.createCell(1).setCellValue(reconcile.getFileType());
            row.createCell(2).setCellValue(reconcile.getFileCode());
            row.createCell(3).setCellValue(reconcile.getFileName());
            row.createCell(4).setCellValue(reconcile.getResult());
            row.createCell(5).setCellValue(reconcile.getCreateDt());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=GDCBReconcile.xlsx");
        response.setStatus(200);
        workBook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workBook.close();
    }

    public void getGDCBReconcileFile(String month, String fileType, String fileName, HttpServletResponse response) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("yearMonth", month);
        requestMap.put("fileType", fileType);
        requestMap.put("fileName", fileName);
        String fullPath = reconcileMapper.getGDCBReconcileFile(requestMap);

        File file = new File(fullPath);
        try {
            InputStream in = new FileInputStream(file);

            response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "\\ ") + "\"");
            response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
            response.setHeader("Content-Length", "" + file.length());

            int cnt = 0;
            byte[] byteArray = new byte[(int) file.length()];

            while ((cnt = in.read(byteArray)) > 0) {
                response.getOutputStream().write(byteArray, 0, cnt);
            }

            in.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
