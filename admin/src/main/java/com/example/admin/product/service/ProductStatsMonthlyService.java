package com.example.admin.product.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.product.dto.InsertProductMonthStat;
import com.example.admin.product.dto.ProductStatsMonthlyDto;
import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.dto.ProductStatsMonthly;
import com.example.admin.product.mapper.ProductStatsDailyMapper;
import com.example.admin.product.mapper.ProductStatsMonthlyMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
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
public class ProductStatsMonthlyService {
    private final ProductStatsMonthlyMapper productStatsMonthlyMapper;
    private final ProductStatsDailyMapper productStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public List<ProductStatsMonthly> getProductStatsMonthlyList(String dcb, String year, String productName) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("year", year);
        requestMap.put("productName", productName == null ? "" : productName);

        return productStatsMonthlyMapper.getProductMonthStats(requestMap);
    }
    public Page<ProductStatsMonthlyDto> getProductStatsMonthlyPage(String dcb, String year, String productName, int page, int pageSize) {
        return functionUtil.toPage(getProductStatsMonthlyDtoList(dcb, year, productName), page, pageSize);
    }

    public List<ProductStatsMonthlyDto> getProductStatsMonthlyDtoList(String dcb, String year, String productName) {
        List<ProductStatsMonthly> productStatsMonthlyList = getProductStatsMonthlyList(dcb, year, productName);

        return toProductStatsMonthlyDtoList(productStatsMonthlyList);
    }

    private List<ProductStatsMonthlyDto> toProductStatsMonthlyDtoList(List<ProductStatsMonthly> productStatsMonthlyList) {
        Map<String, ProductStatsMonthlyDto> productStatsMonthlyDtoMap = new HashMap<>();
        ProductStatsMonthlyDto productStatsMonthlyTotal = ProductStatsMonthlyDto.generateTotal();

        for (ProductStatsMonthly productStatsMonthly : productStatsMonthlyList) {
            String key = productStatsMonthly.getSellerName() + productStatsMonthly.getProductName();
            if(productStatsMonthlyDtoMap.get(key) != null) {
                ProductStatsMonthlyDto dto = productStatsMonthlyDtoMap.get(key);
                dto.addMonthlySales(productStatsMonthly);
                productStatsMonthlyTotal.addTotalMonthlySales(productStatsMonthly);
            } else {
                ProductStatsMonthlyDto dto = ProductStatsMonthlyDto.toProductStatMonthlyDto(productStatsMonthly);
                dto.addMonthlySales(productStatsMonthly);
                productStatsMonthlyDtoMap.put(key, dto);
                productStatsMonthlyTotal.addMonthlySales(productStatsMonthly);
            }
        }

        List<ProductStatsMonthlyDto> productStatsMonthlyDtoList = new ArrayList<>();
        productStatsMonthlyDtoList.add(productStatsMonthlyTotal);

        for (ProductStatsMonthlyDto productStatsMonthlyDto : productStatsMonthlyDtoMap.values()) {
            productStatsMonthlyDto.setPercent(productStatsMonthlyTotal.getTotal());
            productStatsMonthlyDtoList.add(productStatsMonthlyDto);
        }

        return productStatsMonthlyDtoList;
    }

    public void exportProductStatsMonthlyExcel(String dcb, String year, String productName, HttpServletResponse response) throws IOException {
        List<ProductStatsMonthlyDto> productStatsMonthlyDtoList = getProductStatsMonthlyDtoList(dcb, year, productName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("상품명");
        headerRow.createCell(1).setCellValue("판매자 이름");
        headerRow.createCell(2).setCellValue("Total");
        headerRow.createCell(3).setCellValue("비율");

        ProductStatsMonthlyDto productStatsMonthlyDto = productStatsMonthlyDtoList.get(0);
        Map<Integer, Double> map = productStatsMonthlyDto.getMonthlySales();

        for (int i = 1; i <= 12; i++) { // 데이터가 12월까지 없을 경우가 있으므로 누적된 데이터만큼만 셀 생성
            if (map.containsKey(i)) {
                headerRow.createCell(i + 3).setCellValue(i + " 월");
            }
        }

        for (ProductStatsMonthlyDto productStatsMonthly : productStatsMonthlyDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(productStatsMonthly.getSellerName());
            row.createCell(1).setCellValue(productStatsMonthly.getProductName());
            row.createCell(2).setCellValue(productStatsMonthly.getTotal());
            Map<Integer, Double> dtoMap = productStatsMonthly.getMonthlySales();
            for (int i = 1; i <= 12; i++) {
                if (dtoMap.containsKey(i)) {
                    row.createCell(i + 3).setCellValue(dtoMap.get(i));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ProductStatMonthly.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    public void insertStatMonthly(InsertProductMonthStat monthStat) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", monthStat.getYear());
        map.put("merchantName", monthStat.getSellerName());
        map.put("productName", monthStat.getProductName());

        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            if (i < 10) {
                month = "0" + month;
            }
            map.put("month", month);
            List<ProductStatsDaily> productStatsDailyList = productStatsDailyMapper.getProductDayStats(map);
            Map<String, List<ProductStatsDaily>> productStatsMonthlyMap = new HashMap<>();

            for (ProductStatsDaily productStatsDaily : productStatsDailyList) {
                String key = productStatsDaily.getSellerName() + productStatsDaily.getProductName();
                List<ProductStatsDaily> productMonthStatList;

                if (productStatsMonthlyMap.containsKey(key)) {
                    productMonthStatList = productStatsMonthlyMap.get(key);
                } else {
                    productMonthStatList = new ArrayList<>();
                }

                productMonthStatList.add(productStatsDaily);
                productStatsMonthlyMap.put(key, productMonthStatList);
            }

            for (List<ProductStatsDaily> productMonthStatList : productStatsMonthlyMap.values()) {
                double sumPrice = 0;
                double sumTax = 0;
                double sumTotal = 0;

                for (ProductStatsDaily productDayStat : productMonthStatList) {
                    sumPrice += productDayStat.getSumProductPrice();
                    sumTax += productDayStat.getSumTax();
                    sumTotal += productDayStat.getSumTotal();
                }

                ProductStatsMonthly productMonthStat = ProductStatsMonthly.toProductStatsMonthly(monthStat.getYear(), month, monthStat.getSellerName(), monthStat.getProductName(), sumPrice, sumTax, sumTotal);

                productStatsMonthlyMapper.insertProductStatMonthly(productMonthStat);
            }
        }
    }
}
