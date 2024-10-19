package com.example.admin.service.item;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.item.InsertItemMonthStat;
import com.example.admin.domain.dto.item.ItemStatsMonthlyDto;
import com.example.admin.domain.entity.item.ItemStatsDaily;
import com.example.admin.domain.entity.item.ItemStatsMonthly;
import com.example.admin.repository.mapper.item.ItemStatsDailyMapper;
import com.example.admin.repository.mapper.item.ItemStatsMonthlyMapper;
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
public class ItemStatsMonthlyService {
    private final ItemStatsMonthlyMapper itemStatsMonthlyMapper;
    private final ItemStatsDailyMapper itemStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public List<ItemStatsMonthly> getItemStatsMonthlyList(String dcb, String year, String itemName) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("year", year);
        requestMap.put("itemName", itemName == null ? "" : itemName);

        return itemStatsMonthlyMapper.getItemMonthStats(requestMap);
    }
    public Page<ItemStatsMonthlyDto> getItemStatsMonthlyPage(String dcb, String year, String itemName, int page, int pageSize) {
        return functionUtil.toPage(getItemStatsMonthlyDtoList(dcb, year, itemName), page, pageSize);
    }

    public List<ItemStatsMonthlyDto> getItemStatsMonthlyDtoList(String dcb, String year, String itemName) {
        List<ItemStatsMonthly> itemStatsMonthlyList = getItemStatsMonthlyList(dcb, year, itemName);

        return toItemStatsMonthlyDtoList(itemStatsMonthlyList);
    }

    private List<ItemStatsMonthlyDto> toItemStatsMonthlyDtoList(List<ItemStatsMonthly> itemStatsMonthlyList) {
        Map<String, ItemStatsMonthlyDto> itemStatsMonthlyDtoMap = new HashMap<>();
        ItemStatsMonthlyDto itemStatsMonthlyTotal = ItemStatsMonthlyDto.generateTotal();

        for (ItemStatsMonthly itemStatsMonthly : itemStatsMonthlyList) {
            String key = itemStatsMonthly.getMerchantName() + itemStatsMonthly.getItemName();
            if(itemStatsMonthlyDtoMap.get(key) != null) {
                ItemStatsMonthlyDto dto = itemStatsMonthlyDtoMap.get(key);
                dto.addMonthlySales(itemStatsMonthly);
                itemStatsMonthlyTotal.addTotalMonthlySales(itemStatsMonthly);
            } else {
                ItemStatsMonthlyDto dto = ItemStatsMonthlyDto.toItemStatMonthlyDto(itemStatsMonthly);
                dto.addMonthlySales(itemStatsMonthly);
                itemStatsMonthlyDtoMap.put(key, dto);
                itemStatsMonthlyTotal.addMonthlySales(itemStatsMonthly);
            }
        }

        List<ItemStatsMonthlyDto> itemStatsMonthlyDtoList = new ArrayList<>();
        itemStatsMonthlyDtoList.add(itemStatsMonthlyTotal);

        for (ItemStatsMonthlyDto itemStatsMonthlyDto : itemStatsMonthlyDtoMap.values()) {
            itemStatsMonthlyDto.setPercent(itemStatsMonthlyTotal.getTotal());
            itemStatsMonthlyDtoList.add(itemStatsMonthlyDto);
        }

        return itemStatsMonthlyDtoList;
    }

    public void exportItemStatsMonthlyExcel(String dcb, String year, String itemName, HttpServletResponse response) throws IOException {
        List<ItemStatsMonthlyDto> itemStatsMonthlyDtoList = getItemStatsMonthlyDtoList(dcb, year, itemName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("상품명");
        headerRow.createCell(1).setCellValue("판매자 이름");
        headerRow.createCell(2).setCellValue("Total");
        headerRow.createCell(3).setCellValue("비율");

        ItemStatsMonthlyDto itemStatsMonthlyDto = itemStatsMonthlyDtoList.get(0);
        Map<Integer, Double> map = itemStatsMonthlyDto.getMonthlySales();

        for (int i = 1; i <= 12; i++) { // 데이터가 12월까지 없을 경우가 있으므로 누적된 데이터만큼만 셀 생성
            if (map.containsKey(i)) {
                headerRow.createCell(i + 3).setCellValue(i + " 월");
            }
        }

        for (ItemStatsMonthlyDto itemStatsMonthly : itemStatsMonthlyDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemStatsMonthly.getMerchantName());
            row.createCell(1).setCellValue(itemStatsMonthly.getItemName());
            row.createCell(2).setCellValue(itemStatsMonthly.getTotal());
            Map<Integer, Double> dtoMap = itemStatsMonthly.getMonthlySales();
            for (int i = 1; i <= 12; i++) {
                if (dtoMap.containsKey(i)) {
                    row.createCell(i + 3).setCellValue(dtoMap.get(i));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ItemStatMonthly.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    public void insertStatMonthly(InsertItemMonthStat monthStat) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", monthStat.getYear());
        map.put("merchantName", monthStat.getMerchantName());
        map.put("itemName", monthStat.getItemName());

        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            if (i < 10) {
                month = "0" + month;
            }
            map.put("month", month);
            List<ItemStatsDaily> itemStatsDailyList = itemStatsDailyMapper.getItemDayStats(map);
            Map<String, List<ItemStatsDaily>> itemStatsMonthlyMap = new HashMap<>();

            for (ItemStatsDaily itemStatsDaily : itemStatsDailyList) {
                String key = itemStatsDaily.getMerchantName() + itemStatsDaily.getItemName();
                List<ItemStatsDaily> itemMonthStatList;

                if (itemStatsMonthlyMap.containsKey(key)) {
                    itemMonthStatList = itemStatsMonthlyMap.get(key);
                } else {
                    itemMonthStatList = new ArrayList<>();
                }

                itemMonthStatList.add(itemStatsDaily);
                itemStatsMonthlyMap.put(key, itemMonthStatList);
            }

            for (List<ItemStatsDaily> itemMonthStatList : itemStatsMonthlyMap.values()) {
                double sumPrice = 0;
                double sumTax = 0;
                double sumTotal = 0;

                for (ItemStatsDaily itemDayStat : itemMonthStatList) {
                    sumPrice += itemDayStat.getSumItemPrice();
                    sumTax += itemDayStat.getSumTax();
                    sumTotal += itemDayStat.getSumTotal();
                }

                ItemStatsMonthly itemMonthStat = ItemStatsMonthly.toItemStatsMonthly(monthStat.getYear(), month, monthStat.getMerchantName(), monthStat.getItemName(), sumPrice, sumTax, sumTotal);

                itemStatsMonthlyMapper.insertItemStatMonthly(itemMonthStat);
            }
        }
    }
}
