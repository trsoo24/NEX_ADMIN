package com.example.admin.service;

import com.example.admin.domain.dto.payment.DayPaymentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService {

    public void exportDayPaymentExcel(Map<String, List<Object>> dayPaymentMap, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("일별 통계");

        Row headerRow = sheet.createRow(0);

        Field[] fields = DayPaymentDto.class.getDeclaredFields();

        for (int i = 0 ; i < fields.length ; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName());
        }

        for (int j = 0; j < dayPaymentMap.size(); j++) {
            Row row = sheet.createRow(j + 1);
            List<Object> objects = dayPaymentMap.get(fields[j].getName());
            for (int k = 0; k < objects.size(); k++) {
                row.createCell(k).setCellValue(objects.get(k).toString());
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }
}
