package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.InsertSellerMonthStat;
import com.example.admin.seller.dto.SellerMonthStatDto;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.dto.SellerMonthStat;
import com.example.admin.seller.mapper.SellerStatsDailyMapper;
import com.example.admin.seller.mapper.SellerStatsMonthlyMapper;
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
public class SellerStatsMonthlyService {
    private final SellerStatsMonthlyMapper sellerStatsMonthlyMapper;
    private final SellerStatsDailyMapper sellerStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public Page<SellerMonthStatDto> getSellerStatsMonthlyPage(String dcb, String year, String sellerName, int page, int pageSize) {
        return functionUtil.toPage(getSellerStatsDtoMonthly(dcb, year, sellerName), page, pageSize);
    }

    public List<SellerMonthStat> getSellerStatsMonthly(String dcb, String year, String sellerName) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("year", year);
        requestMap.put("sellerName", sellerName == null ? "" : sellerName);

        return sellerStatsMonthlyMapper.getSellerMonthStats(requestMap);
    }

    public List<SellerMonthStatDto> getSellerStatsDtoMonthly(String dcb, String year, String sellerName) {
        List<SellerMonthStat> sellerMonthStatList = getSellerStatsMonthly(dcb, year, sellerName);

        return toSellerMonthStatDtoList(sellerMonthStatList);
    }

    private List<SellerMonthStatDto> toSellerMonthStatDtoList(List<SellerMonthStat> sellerMonthStatList) {
        Map<String , SellerMonthStatDto> itemMonthStatDtoMap = new HashMap<>();
        SellerMonthStatDto sellerMonthStatTotal = SellerMonthStatDto.generateTotal();

        for (SellerMonthStat sellerMonthStat : sellerMonthStatList) {
            if (itemMonthStatDtoMap.get(sellerMonthStat.getSellerName()) != null) {
                SellerMonthStatDto sellerMonthStatDto = itemMonthStatDtoMap.get(sellerMonthStat.getSellerName());
                sellerMonthStatDto.addMonthlySales(sellerMonthStat);
                sellerMonthStatTotal.addTotalMonthlySales(sellerMonthStat);
            } else {
                SellerMonthStatDto sellerMonthStatDto = SellerMonthStatDto.toSellerMonthStatDto(sellerMonthStat);
                sellerMonthStatDto.addMonthlySales(sellerMonthStat);
                itemMonthStatDtoMap.put(sellerMonthStat.getSellerName(), sellerMonthStatDto);
                sellerMonthStatTotal.addTotalMonthlySales(sellerMonthStat);
            }
        }
        List<SellerMonthStatDto> sellerMonthStatDtoList = new ArrayList<>();
        sellerMonthStatDtoList.add(sellerMonthStatTotal);

        for (SellerMonthStatDto sellerMonthStatDto : itemMonthStatDtoMap.values()) {
            sellerMonthStatDto.setPercent(sellerMonthStatTotal.getTotal());
            sellerMonthStatDtoList.add(sellerMonthStatDto);
        }

        return sellerMonthStatDtoList;
    }

    public void exportSellerStatMonthlyExcel(String dcb, String year, String sellerName, HttpServletResponse response) throws IOException {
        List<SellerMonthStatDto> sellerMonthStatDtoList = getSellerStatsDtoMonthly(dcb, year, sellerName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 월별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("판매자 이름");
        headerRow.createCell(1).setCellValue("Total");
        headerRow.createCell(2).setCellValue("비율");

        SellerMonthStatDto sellerMonthStatDto = sellerMonthStatDtoList.get(0);
        Map<Integer, Double> map = sellerMonthStatDto.getMonthlySales();

        for (int i = 1; i <= 12; i++) { // 데이터가 12월까지 없을 경우가 있으므로 누적된 데이터만큼만 셀 생성
            if (map.containsKey(i)) {
                headerRow.createCell(i + 2).setCellValue(i + " 월");
            }
        }

        for (SellerMonthStatDto itemMonthStat : sellerMonthStatDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemMonthStat.getSellerName());
            row.createCell(1).setCellValue(itemMonthStat.getTotal());
            row.createCell(2).setCellValue(itemMonthStat.getPercent());
            Map<Integer, Double> dtoMap = itemMonthStat.getMonthlySales();
            for (int i = 1; i <= 12; i++) {
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


    public void insertMonthlyStat(InsertSellerMonthStat monthStat) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", monthStat.getYear());
        map.put("sellerName", monthStat.getSellerName());

        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            if (i < 10) {
                month = "0" + month;
            }
            map.put("month", month);
            List<SellerDayStat> dayStatList = sellerStatsDailyMapper.getSellerDayStats(map);
            Map<String, List<SellerDayStat>> sellerMap = new HashMap<>();

            for (SellerDayStat sellerDayStat : dayStatList) {
                if (sellerMap.containsKey(sellerDayStat.getSellerName())) {
                    List<SellerDayStat> sellerMonthStatList = sellerMap.get(sellerDayStat.getSellerName());
                    sellerMonthStatList.add(sellerDayStat);
                    sellerMap.put(sellerDayStat.getSellerName(), sellerMonthStatList);
                } else {
                    List<SellerDayStat> sellerMonthStatList = new ArrayList<>();
                    sellerMonthStatList.add(sellerDayStat);
                    sellerMap.put(sellerDayStat.getSellerName(), sellerMonthStatList);
                }
            }

            for (List<SellerDayStat> sellerMonthStatList : sellerMap.values()) {
                double sumPrice = 0;
                double sumTax = 0;
                double sumTotal = 0;

                for (SellerDayStat sellerDayStat : sellerMonthStatList) {
                    sumPrice += sellerDayStat.getSumPrice();
                    sumTax += sellerDayStat.getSumTax();
                    sumTotal += sellerDayStat.getSumTotal();
                }

                SellerMonthStat sellerMonthStat = SellerMonthStat.toMonthStat(monthStat.getYear(), month, monthStat.getSellerName(), sumPrice, sumTax, sumTotal);

                sellerStatsMonthlyMapper.insertSellerMonthStat(sellerMonthStat);
            }
        }
    }
}
