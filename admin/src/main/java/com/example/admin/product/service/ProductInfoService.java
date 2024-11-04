package com.example.admin.product.service;

import com.example.admin.product.dto.InsertProductInfoDto;
import com.example.admin.product.dto.ProductInfo;
import com.example.admin.product.dto.field.ProductInfoField;
import com.example.admin.product.mapper.ProductInfoMapper;
import com.example.admin.common.service.FunctionUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductInfoService {
    private final ProductInfoMapper productInfoMapper;
    private final FunctionUtil functionUtil;

    @Transactional
    public void createProductInfo(InsertProductInfoDto dto) {
        String original = dto.getProductName();
        for (int i = 1; i < 100; i++) {
            String productName = dto.getProductName() + i;
            Double price = new Random().nextDouble();
            dto.setProductName(productName);
            dto.setPrice(price);
            productInfoMapper.insertProductInfo(dto);
            dto.setProductName(original);
        }
    }

    private boolean existProduct(String productName) {
        return productInfoMapper.existsProduct(productName);
    }

    private List<ProductInfo> getProductList(Map<String, String> requestMap) {
        return productInfoMapper.getAllProductInfo(requestMap);
    }

    public Page<ProductInfo> getAllProductInfo(String dcb, String productName, String startDate, String endDate, int page, int pageSize) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("productName", productName);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        List<ProductInfo> productInfoList = getProductList(requestMap);

        return functionUtil.toPage(productInfoList, page, pageSize);
    }

    public void exportExcel(String dcb, String productName, String startDate, String endDate, HttpServletResponse response) throws IllegalAccessException, IOException {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("productName", productName);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        List<ProductInfo> productInfoList = getProductList(requestMap);

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
    }
}
