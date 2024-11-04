package com.example.admin.product.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.product.dto.InsertProductDayStat;
import com.example.admin.product.dto.ProductStatsDailyDto;
import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.mapper.ProductStatsDailyMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductStatsDailyService {
    private final ProductStatsDailyMapper productStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public List<ProductStatsDaily> getProductStatsDailyList(String dcb, String month, String productName) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] yearAndMonth = month.split("-");
        requestMap.put("dcb", dcb);
        requestMap.put("year", yearAndMonth[0]);
        requestMap.put("month", yearAndMonth[1]);
//        requestMap.put("sellerName", sellerName == null ? "" : sellerName);
        requestMap.put("productName", productName == null ? "" : productName);

        return productStatsDailyMapper.getProductDayStats(requestMap);
    }
    public Page<ProductStatsDailyDto> getProductStatsDailyPage(String dcb, String month, String productName, int page, int pageSize) {
        List<ProductStatsDaily> productStatsDailyList = getProductStatsDailyList(dcb, month, productName);

        return functionUtil.toPage(transProductStatsDailyDtoList(productStatsDailyList), page, pageSize);
    }

    public List<ProductStatsDailyDto> transProductStatsDailyDtoList(List<ProductStatsDaily> productStatsDailyList) {
        return toProductStatsDailyDtoList(productStatsDailyList);
    }

    private List<ProductStatsDailyDto> toProductStatsDailyDtoList(List<ProductStatsDaily> productStatsDailyList) {
        Map<String, ProductStatsDailyDto> productStatsDailyDtoMap = new HashMap<>();
        ProductStatsDailyDto productStatsDailyTotal = ProductStatsDailyDto.generateTotal();

        for (ProductStatsDaily productStatsDaily : productStatsDailyList) {
            String key = productStatsDaily.getSellerName() + productStatsDaily.getProductName();
            if (productStatsDailyDtoMap.get(key) != null) {
                ProductStatsDailyDto dto = productStatsDailyDtoMap.get(key);
                dto.addDailySales(productStatsDaily);
                productStatsDailyTotal.addTotalDailySales(productStatsDaily);
            } else {
                ProductStatsDailyDto dto = ProductStatsDailyDto.toProductStatDailyDto(productStatsDaily);
                dto.addDailySales(productStatsDaily);
                productStatsDailyDtoMap.put(key, dto);
                productStatsDailyTotal.addTotalDailySales(productStatsDaily);
            }
        }

        List<ProductStatsDailyDto> productStatsDailyDtoList = new ArrayList<>();
        productStatsDailyDtoList.add(productStatsDailyTotal);

        for (ProductStatsDailyDto productStatsDailyDto : productStatsDailyDtoMap.values()) {
            productStatsDailyDto.setPercent(productStatsDailyTotal.getTotal());
            productStatsDailyDtoList.add(productStatsDailyDto);
        }

        return productStatsDailyDtoList;
    }

    public void exportProductStatDailyExcel(String dcb, String month, String productName, HttpServletResponse response) throws IOException {
        List<ProductStatsDaily> productStatsDailyList = getProductStatsDailyList(dcb, month, productName);

        List<ProductStatsDailyDto> productStatsDailyDtoList = transProductStatsDailyDtoList(productStatsDailyList);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("상품명");
        headerRow.createCell(1).setCellValue("판매자 이름");
        headerRow.createCell(2).setCellValue("Total");
        headerRow.createCell(3).setCellValue("비율");

        ProductStatsDailyDto productStatsDailyDto = productStatsDailyDtoList.get(0);
        Map<Integer, Double> map = productStatsDailyDto.getDailySales();

        for (int i = 1; i <= 31; i++) {
            if(map.containsKey(i)) {
                headerRow.createCell(i + 3).setCellValue(i + " 일");
            }
        }

        for (ProductStatsDailyDto productStatsDaily : productStatsDailyDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(productStatsDaily.getProductName());
            row.createCell(1).setCellValue(productStatsDaily.getSellerName());
            row.createCell(2).setCellValue(productStatsDaily.getTotal());
            row.createCell(3).setCellValue(productStatsDaily.getPercent());
            Map<Integer, Double> dtoMap = productStatsDaily.getDailySales();
            for (int i = 1; i <= 31; i++) {
                if(dtoMap.containsKey(i)) {
                    row.createCell(i + 3).setCellValue(dtoMap.get(i));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ProductStatsDaily.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    public void insertStatDaily(InsertProductDayStat productDayStat) {
        Random random = new Random();

        for (int i = 1; i <= 31; i++) {
            double randomPrice = Math.floor(10000 + random.nextDouble() * 90000);
            double randomTax = Math.floor(100 + random.nextDouble() * 900);
            String day = String.valueOf(i);
            if (i < 10) {
                day = "0" + day;
            }
            ProductStatsDaily productStatsDaily = ProductStatsDaily.toProductStatsDaily(productDayStat.getYear(), productDayStat.getMonth(), day, productDayStat.getSellerName(), productDayStat.getProductName(), randomPrice, randomTax, randomPrice - randomTax);

            productStatsDailyMapper.insertProductStatDaily(productStatsDaily);
        }
    }
}
