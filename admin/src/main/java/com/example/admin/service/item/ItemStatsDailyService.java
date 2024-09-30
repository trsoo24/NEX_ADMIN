package com.example.admin.service.item;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.item.InsertItemDayStat;
import com.example.admin.domain.dto.item.ItemStatsDailyDto;
import com.example.admin.domain.entity.item.ItemStatsDaily;
import com.example.admin.repository.mapper.item.ItemStatsDailyMapper;
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
public class ItemStatsDailyService {
    private final ItemStatsDailyMapper itemStatsDailyMapper;
    private final FunctionUtil functionUtil;

    public Page<ItemStatsDailyDto> getItemStatsDailyPage(String dcb, String month, String itemName, int page, int pageSize) {
        return functionUtil.toPage(getItemStatsDailyList(dcb, month, itemName), page, pageSize);
    }

    public List<ItemStatsDailyDto> getItemStatsDailyList(String dcb, String month, String itemName) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] yearAndMonth = month.split("-");
        requestMap.put("dcb", dcb);
        requestMap.put("year", yearAndMonth[0]);
        requestMap.put("month", yearAndMonth[1]);
//        requestMap.put("merchantName", merchantName == null ? "" : merchantName);
        requestMap.put("itemName", itemName == null ? "" : itemName);

        List<ItemStatsDaily> itemStatsDailyList = itemStatsDailyMapper.getItemDayStats(requestMap);

        return toItemStatsDailyDtoList(itemStatsDailyList);
    }

    private List<ItemStatsDailyDto> toItemStatsDailyDtoList(List<ItemStatsDaily> itemStatsDailyList) {
        Map<String, ItemStatsDailyDto> itemStatsDailyDtoMap = new HashMap<>();
        ItemStatsDailyDto itemStatsDailyTotal = ItemStatsDailyDto.generateTotal();

        for (ItemStatsDaily itemStatsDaily : itemStatsDailyList) {
            String key = itemStatsDaily.getMerchantName() + itemStatsDaily.getItemName();
            if (itemStatsDailyDtoMap.get(key) != null) {
                ItemStatsDailyDto dto = itemStatsDailyDtoMap.get(key);
                dto.addDailySales(itemStatsDaily);
                itemStatsDailyTotal.addTotalDailySales(itemStatsDaily);
            } else {
                ItemStatsDailyDto dto = ItemStatsDailyDto.toItemStatDailyDto(itemStatsDaily);
                dto.addDailySales(itemStatsDaily);
                itemStatsDailyDtoMap.put(key, dto);
                itemStatsDailyTotal.addTotalDailySales(itemStatsDaily);
            }
        }

        List<ItemStatsDailyDto> itemStatsDailyDtoList = new ArrayList<>();
        itemStatsDailyDtoList.add(itemStatsDailyTotal);

        for (ItemStatsDailyDto itemStatsDailyDto : itemStatsDailyDtoMap.values()) {
            itemStatsDailyDto.setPercent(itemStatsDailyTotal.getTotal());
            itemStatsDailyDtoList.add(itemStatsDailyDto);
        }

        return itemStatsDailyDtoList;
    }

    public void exportItemStatDailyExcel(String dcb, String month, String itemName, HttpServletResponse response) throws IOException {
        List<ItemStatsDailyDto> itemStatsDailyDtoList = getItemStatsDailyList(dcb, month, itemName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품 일별 판매 현황");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("상품명");
        headerRow.createCell(1).setCellValue("판매자 이름");
        headerRow.createCell(2).setCellValue("Total");
        headerRow.createCell(3).setCellValue("비율");

        ItemStatsDailyDto itemStatsDailyDto = itemStatsDailyDtoList.get(0);
        Map<Integer, Double> map = itemStatsDailyDto.getDailySales();

        for (int i = 1; i <= 31; i++) {
            if(map.containsKey(i)) {
                headerRow.createCell(i + 3).setCellValue(i + " 일");
            }
        }

        for (ItemStatsDailyDto itemStatsDaily : itemStatsDailyDtoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(itemStatsDaily.getItemName());
            row.createCell(1).setCellValue(itemStatsDaily.getMerchantName());
            row.createCell(2).setCellValue(itemStatsDaily.getTotal());
            row.createCell(3).setCellValue(itemStatsDaily.getPercent());
            Map<Integer, Double> dtoMap = itemStatsDaily.getDailySales();
            for (int i = 1; i <= 31; i++) {
                if(dtoMap.containsKey(i)) {
                    row.createCell(i + 3).setCellValue(dtoMap.get(i));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ItemStatsDaily.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    public void insertStatDaily(InsertItemDayStat itemDayStat) {
        Random random = new Random();

        for (int i = 1; i <= 31; i++) {
            double randomPrice = Math.floor(10000 + random.nextDouble() * 90000);
            double randomTax = Math.floor(100 + random.nextDouble() * 900);
            String day = String.valueOf(i);
            if (i < 10) {
                day = "0" + day;
            }
            ItemStatsDaily itemStatsDaily = ItemStatsDaily.toItemStatsDaily(itemDayStat.getYear(), itemDayStat.getMonth(), day, itemDayStat.getMerchantName(), itemDayStat.getItemName(), randomPrice, randomTax, randomPrice - randomTax);

            itemStatsDailyMapper.insertItemStatDaily(itemStatsDaily);
        }
    }
}
