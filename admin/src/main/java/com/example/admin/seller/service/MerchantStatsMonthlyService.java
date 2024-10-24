package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.InsertMerchantMonthStat;
import com.example.admin.seller.dto.MerchantMonthStatDto;
import com.example.admin.seller.dto.MerchantDayStat;
import com.example.admin.seller.dto.MerchantMonthStat;
import com.example.admin.seller.mapper.MerchantStatsDailyMapper;
import com.example.admin.seller.mapper.MerchantStatsMonthlyMapper;
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
public class MerchantStatsMonthlyService {
    private final MerchantStatsMonthlyMapper merchantStatsMonthlyMapper;
    private final MerchantStatsDailyMapper merchantStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public Page<MerchantMonthStatDto> getMerchantStatsMonthlyPage(String dcb, String year, String merchantName, int page, int pageSize) {
        return functionUtil.toPage(getMerchantStatsDtoMonthly(dcb, year, merchantName), page, pageSize);
    }

    public List<MerchantMonthStat> getMerchantStatsMonthly(String dcb, String year, String merchantName) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("year", year);
        requestMap.put("merchantName", merchantName == null ? "" : merchantName);

        return merchantStatsMonthlyMapper.getMerchantMonthStats(requestMap);
    }

    public List<MerchantMonthStatDto> getMerchantStatsDtoMonthly(String dcb, String year, String merchantName) {
        List<MerchantMonthStat> merchantMonthStatList = getMerchantStatsMonthly(dcb, year, merchantName);

        return toMerchantMonthStatDtoList(merchantMonthStatList);
    }

    private List<MerchantMonthStatDto> toMerchantMonthStatDtoList(List<MerchantMonthStat> merchantMonthStatList) {
        Map<String , MerchantMonthStatDto> itemMonthStatDtoMap = new HashMap<>();
        MerchantMonthStatDto merchantMonthStatTotal = MerchantMonthStatDto.generateTotal();

        for (MerchantMonthStat merchantMonthStat : merchantMonthStatList) {
            if (itemMonthStatDtoMap.get(merchantMonthStat.getMerchantName()) != null) {
                MerchantMonthStatDto merchantMonthStatDto = itemMonthStatDtoMap.get(merchantMonthStat.getMerchantName());
                merchantMonthStatDto.addMonthlySales(merchantMonthStat);
                merchantMonthStatTotal.addTotalMonthlySales(merchantMonthStat);
            } else {
                MerchantMonthStatDto merchantMonthStatDto = MerchantMonthStatDto.toMerchantMonthStatDto(merchantMonthStat);
                merchantMonthStatDto.addMonthlySales(merchantMonthStat);
                itemMonthStatDtoMap.put(merchantMonthStat.getMerchantName(), merchantMonthStatDto);
                merchantMonthStatTotal.addTotalMonthlySales(merchantMonthStat);
            }
        }
        List<MerchantMonthStatDto> merchantMonthStatDtoList = new ArrayList<>();
        merchantMonthStatDtoList.add(merchantMonthStatTotal);

        for (MerchantMonthStatDto merchantMonthStatDto : itemMonthStatDtoMap.values()) {
            merchantMonthStatDto.setPercent(merchantMonthStatTotal.getTotal());
            merchantMonthStatDtoList.add(merchantMonthStatDto);
        }

        return merchantMonthStatDtoList;
    }

    public void exportMerchantStatMonthlyExcel(String dcb, String year, String merchantName, HttpServletResponse response) throws IOException {
        List<MerchantMonthStatDto> merchantMonthStatDtoList = getMerchantStatsDtoMonthly(dcb, year, merchantName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 월별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("판매자 이름");
        headerRow.createCell(1).setCellValue("Total");
        headerRow.createCell(2).setCellValue("비율");

        MerchantMonthStatDto merchantMonthStatDto = merchantMonthStatDtoList.get(0);
        Map<Integer, Double> map = merchantMonthStatDto.getMonthlySales();

        for (int i = 1; i <= 12; i++) { // 데이터가 12월까지 없을 경우가 있으므로 누적된 데이터만큼만 셀 생성
            if (map.containsKey(i)) {
                headerRow.createCell(i + 2).setCellValue(i + " 월");
            }
        }

        for (MerchantMonthStatDto itemMonthStat : merchantMonthStatDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemMonthStat.getMerchantName());
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
        response.setHeader("Content-Disposition", "attachment; filename=MerchantStatDaily.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }


    public void insertMonthlyStat(InsertMerchantMonthStat monthStat) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", monthStat.getYear());
        map.put("merchantName", monthStat.getMerchantName());

        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            if (i < 10) {
                month = "0" + month;
            }
            map.put("month", month);
            List<MerchantDayStat> dayStatList = merchantStatsDailyMapper.getMerchantDayStats(map);
            Map<String, List<MerchantDayStat>> merchantMap = new HashMap<>();

            for (MerchantDayStat merchantDayStat : dayStatList) {
                if (merchantMap.containsKey(merchantDayStat.getMerchantName())) {
                    List<MerchantDayStat> merchantMonthStatList = merchantMap.get(merchantDayStat.getMerchantName());
                    merchantMonthStatList.add(merchantDayStat);
                    merchantMap.put(merchantDayStat.getMerchantName(), merchantMonthStatList);
                } else {
                    List<MerchantDayStat> merchantMonthStatList = new ArrayList<>();
                    merchantMonthStatList.add(merchantDayStat);
                    merchantMap.put(merchantDayStat.getMerchantName(), merchantMonthStatList);
                }
            }

            for (List<MerchantDayStat> merchantMonthStatList : merchantMap.values()) {
                double sumPrice = 0;
                double sumTax = 0;
                double sumTotal = 0;

                for (MerchantDayStat merchantDayStat : merchantMonthStatList) {
                    sumPrice += merchantDayStat.getSumPrice();
                    sumTax += merchantDayStat.getSumTax();
                    sumTotal += merchantDayStat.getSumTotal();
                }

                MerchantMonthStat merchantMonthStat = MerchantMonthStat.toMonthStat(monthStat.getYear(), month, monthStat.getMerchantName(), sumPrice, sumTax, sumTotal);

                merchantStatsMonthlyMapper.insertMerchantMonthStat(merchantMonthStat);
            }
        }
    }
}
