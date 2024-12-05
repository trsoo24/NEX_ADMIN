package com.example.admin.product.service;

import com.example.admin.product.dto.ProductInfo;
import com.example.admin.product.dto.field.ProductInfoField;
import com.example.admin.product.mapper.ProductInfoMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInfoService {
    private final ProductInfoMapper productInfoMapper;

    public List<ProductInfo> getProductInfoList(String productName, String startDate, String endDate, boolean isExcel) {
        String trxNo = MDC.get("trxNo");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productName", productName);
        requestMap.put("startDate", dateForm(startDate));
        requestMap.put("endDate", dateForm(endDate));

        log.info("[{}] 요청 = {} 부터 {} 까지 상품 목록 조회", trxNo, startDate, endDate);
        List<ProductInfo> productInfoList = productInfoMapper.getAllProductInfo(requestMap);

        if (!isExcel) {
            log.info("[{}] 응답 = 상품 목록 {} 건 조회 완료", trxNo, productInfoList.size());
        }

        return productInfoList;
    }

    public void exportExcel(String productName, String startDate, String endDate, HttpServletResponse response) throws IllegalAccessException, IOException {
        String trxNo = MDC.get("trxNo");

        List<ProductInfo> productInfoList = getProductInfoList(productName, startDate, endDate, true);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품 조회");
        XSSFRow header = sheet.createRow(1);

        ProductInfoField[] productFields = ProductInfoField.values();
        Field[] productInfoFields = ProductInfo.class.getDeclaredFields();

        header.createCell(0).setCellValue("번호");
        for (int i = 0; i < productFields.length; i++) {
            header.createCell(i + 1).setCellValue(productFields[i].getDescription());
        }

        int rowIdx = 2;

        for (int j = 0; j < productInfoList.size(); j++) {
            ProductInfo productInfo = productInfoList.get(j);
            XSSFRow row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(j + 1);

            for (int i = 0; i < productFields.length; i++) {
                Field field = productInfoFields[i];
                field.setAccessible(true);
                Object value = field.get(productInfo);

                if (value instanceof Double) {
                    row.createCell(i + 1).setCellValue((Double) value);
                } else {
                    row.createCell(i + 1).setCellValue((String) value);
                }
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ProductInfo.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();

        log.info("[{}] 응답 = 상품 목록 Excel 생성 완료", trxNo);
    }

    private String dateForm(String date) {
        if (date.contains("-")) {
            return date.replaceAll("-", "");
        } else {
            return date;
        }
    }
}
