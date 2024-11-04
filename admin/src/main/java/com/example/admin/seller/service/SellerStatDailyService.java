package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.InsertSellerDayStat;
import com.example.admin.seller.dto.SellerDayStatDto;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.mapper.SellerStatsDailyMapper;
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
public class SellerStatDailyService {
    private final SellerStatsDailyMapper sellerStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public List<SellerDayStat> getSellerStatsDaily(String dcb, String month, String sellerName) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] yearAndMonth = month.split("-");
        requestMap.put("dcb", dcb);
        requestMap.put("year", yearAndMonth[0]);
        requestMap.put("month", yearAndMonth[1]);
        requestMap.put("sellerName", sellerName == null ? "" : sellerName);

        return sellerStatsDailyMapper.getSellerDayStats(requestMap);
    }
    public Page<SellerDayStatDto> getSellerStatDailyPage(String dcb, String month, String sellerName, int page, int pageSize) {
        return functionUtil.toPage(getSellerStatDtoDaily(dcb, month, sellerName), page, pageSize);
    }

    public List<SellerDayStatDto> getSellerStatDtoDaily(String dcb, String month, String sellerName) {
        List<SellerDayStat> sellerDayStatList = getSellerStatsDaily(dcb, month, sellerName);

        return toSellerDayStatDtoList(sellerDayStatList);
    }

    private List<SellerDayStatDto> toSellerDayStatDtoList(List<SellerDayStat> sellerDayStatList) {
        Map<String, SellerDayStatDto> sellerDayStatDtoMap = new HashMap<>();
        SellerDayStatDto sellerDayStatTotal = SellerDayStatDto.generateTotal();

        for (SellerDayStat sellerDayStat : sellerDayStatList) {
            if (sellerDayStatDtoMap.get(sellerDayStat.getSellerName()) != null) {
                SellerDayStatDto sellerDayStatDto = sellerDayStatDtoMap.get(sellerDayStat.getSellerName());
                sellerDayStatDto.addDailySales(sellerDayStat);
                sellerDayStatTotal.addTotalDailySales(sellerDayStat);
            } else {
                SellerDayStatDto sellerDayStatDto = SellerDayStatDto.toSellerDayStatDto(sellerDayStat);
                sellerDayStatDto.addDailySales(sellerDayStat);
                sellerDayStatDtoMap.put(sellerDayStat.getSellerName(), sellerDayStatDto);
                sellerDayStatTotal.addTotalDailySales(sellerDayStat);
            }
        }

        List<SellerDayStatDto> sellerDayStatDtoList = new ArrayList<>();
        sellerDayStatDtoList.add(sellerDayStatTotal);

        for (SellerDayStatDto sellerDayStatDto : sellerDayStatDtoMap.values()) {
            sellerDayStatDto.setPercent(sellerDayStatTotal.getTotal());
            sellerDayStatDtoList.add(sellerDayStatDto);
        }

        return sellerDayStatDtoList;
    }

    public void exportSellerStatDailyExcel(String dcb, String month, String sellerName, HttpServletResponse response) throws IOException {
        List<SellerDayStatDto> sellerDayStatDtoList = getSellerStatDtoDaily(dcb, month, sellerName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("판매자 이름");
        headerRow.createCell(1).setCellValue("Total");
        headerRow.createCell(2).setCellValue("비율");

        SellerDayStatDto sellerDayStatDto = sellerDayStatDtoList.get(0);
        Map<Integer, Double> map = sellerDayStatDto.getDailySales();

        for (int i = 1; i <= 31; i++) {
            if (map.containsKey(i)) {
                headerRow.createCell(i + 2).setCellValue(i + " 일");
            }
        }

        for (SellerDayStatDto itemDayStat : sellerDayStatDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemDayStat.getSellerName());
            row.createCell(1).setCellValue(itemDayStat.getTotal());
            row.createCell(2).setCellValue(itemDayStat.getPercent());
            Map<Integer, Double> dtoMap = itemDayStat.getDailySales();
            for (int i = 1; i <= 31; i++) {
                if (map.containsKey(i)) {
                    row.createCell(i + 2).setCellValue(dtoMap.get(i));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=SellerStatDaily.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }



    public void insertTestValue(InsertSellerDayStat dayStat) {
        Random random = new Random();

        for (int i = 1; i <= 31; i++) {
            double randomPrice = Math.floor(100000 + random.nextDouble() * 900000);
            double randomTax = Math.floor(1000 + random.nextDouble() * 9000);
            String day = String.valueOf(i);
            if (i < 10) {
                day = "0" + day;
            }
            SellerDayStat sellerDayStat = SellerDayStat.to(dayStat.getYear(), dayStat.getMonth(), day, dayStat.getSellerName(), randomPrice, randomTax, randomPrice - randomTax);
            sellerStatsDailyMapper.insertSellerDayStat(sellerDayStat);
        }
    }
}
