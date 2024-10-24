package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.InsertMerchantDayStat;
import com.example.admin.seller.dto.MerchantDayStatDto;
import com.example.admin.seller.dto.MerchantDayStat;
import com.example.admin.seller.mapper.MerchantStatsDailyMapper;
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
public class MerchantStatDailyService {
    private final MerchantStatsDailyMapper merchantStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public List<MerchantDayStat> getMerchantStatsDaily(String dcb, String month, String merchantName) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] yearAndMonth = month.split("-");
        requestMap.put("dcb", dcb);
        requestMap.put("year", yearAndMonth[0]);
        requestMap.put("month", yearAndMonth[1]);
        requestMap.put("merchantName", merchantName == null ? "" : merchantName);

        return merchantStatsDailyMapper.getMerchantDayStats(requestMap);
    }
    public Page<MerchantDayStatDto> getMerchantStatDailyPage(String dcb, String month, String merchantName, int page, int pageSize) {
        return functionUtil.toPage(getMerchantStatDtoDaily(dcb, month, merchantName), page, pageSize);
    }

    public List<MerchantDayStatDto> getMerchantStatDtoDaily(String dcb, String month, String merchantName) {
        List<MerchantDayStat> merchantDayStatList = getMerchantStatsDaily(dcb, month, merchantName);

        return toMerchantDayStatDtoList(merchantDayStatList);
    }

    private List<MerchantDayStatDto> toMerchantDayStatDtoList(List<MerchantDayStat> merchantDayStatList) {
        Map<String, MerchantDayStatDto> merchantDayStatDtoMap = new HashMap<>();
        MerchantDayStatDto merchantDayStatTotal = MerchantDayStatDto.generateTotal();

        for (MerchantDayStat merchantDayStat : merchantDayStatList) {
            if (merchantDayStatDtoMap.get(merchantDayStat.getMerchantName()) != null) {
                MerchantDayStatDto merchantDayStatDto = merchantDayStatDtoMap.get(merchantDayStat.getMerchantName());
                merchantDayStatDto.addDailySales(merchantDayStat);
                merchantDayStatTotal.addTotalDailySales(merchantDayStat);
            } else {
                MerchantDayStatDto merchantDayStatDto = MerchantDayStatDto.toMerchantDayStatDto(merchantDayStat);
                merchantDayStatDto.addDailySales(merchantDayStat);
                merchantDayStatDtoMap.put(merchantDayStat.getMerchantName(), merchantDayStatDto);
                merchantDayStatTotal.addTotalDailySales(merchantDayStat);
            }
        }

        List<MerchantDayStatDto> merchantDayStatDtoList = new ArrayList<>();
        merchantDayStatDtoList.add(merchantDayStatTotal);

        for (MerchantDayStatDto merchantDayStatDto : merchantDayStatDtoMap.values()) {
            merchantDayStatDto.setPercent(merchantDayStatTotal.getTotal());
            merchantDayStatDtoList.add(merchantDayStatDto);
        }

        return merchantDayStatDtoList;
    }

    public void exportMerchantStatDailyExcel(String dcb, String month, String merchantName, HttpServletResponse response) throws IOException {
        List<MerchantDayStatDto> merchantDayStatDtoList = getMerchantStatDtoDaily(dcb, month, merchantName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("판매자 이름");
        headerRow.createCell(1).setCellValue("Total");
        headerRow.createCell(2).setCellValue("비율");

        MerchantDayStatDto merchantDayStatDto = merchantDayStatDtoList.get(0);
        Map<Integer, Double> map = merchantDayStatDto.getDailySales();

        for (int i = 1; i <= 31; i++) {
            if (map.containsKey(i)) {
                headerRow.createCell(i + 2).setCellValue(i + " 일");
            }
        }

        for (MerchantDayStatDto itemDayStat : merchantDayStatDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemDayStat.getMerchantName());
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
        response.setHeader("Content-Disposition", "attachment; filename=MerchantStatDaily.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }



    public void insertTestValue(InsertMerchantDayStat dayStat) {
        Random random = new Random();

        for (int i = 1; i <= 31; i++) {
            double randomPrice = Math.floor(100000 + random.nextDouble() * 900000);
            double randomTax = Math.floor(1000 + random.nextDouble() * 9000);
            String day = String.valueOf(i);
            if (i < 10) {
                day = "0" + day;
            }
            MerchantDayStat merchantDayStat = MerchantDayStat.to(dayStat.getYear(), dayStat.getMonth(), day, dayStat.getMerchantName(), randomPrice, randomTax, randomPrice - randomTax);
            merchantStatsDailyMapper.insertMerchantDayStat(merchantDayStat);
        }
    }
}
