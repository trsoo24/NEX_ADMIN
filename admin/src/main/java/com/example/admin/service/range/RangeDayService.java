package com.example.admin.service.range;

import com.example.admin.domain.dto.range.RangeDayDto;
import com.example.admin.domain.dto.range.field.RangeDayField;
import com.example.admin.domain.entity.range.RangeDay;
import com.example.admin.repository.mapper.range.RangeDayMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RangeDayService {
    private final RangeDayMapper rangeDayMapper;
    private final String[] A_STAT_ARRAY = {"1", "2", "3", "4", "5", "O"};
    private final Double[] AVERAGE_VALUE_ARRAY = {100000.0, 200000.0, 300000.0, 400000.0, 500000.0, 810000.0};

    public Map<String, List<RangeDayDto>> getRangeDayList(String startDate, String endDate, String dcb) throws IllegalAccessException {
        Map<String, String> paramMap = new HashMap<>(); // 요청 쿼리
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        List<RangeDay> rangeDayList = rangeDayMapper.getRangeDayList(paramMap);
        Map<String ,List<RangeDayDto>> responseMap = new LinkedHashMap<>();

        for (RangeDay rangeDay : rangeDayList) {
            List<RangeDayDto> dtoList = new ArrayList<>();
            if (responseMap.containsKey(rangeDay.getStatDay())) {
                dtoList = responseMap.get(rangeDay.getStatDay());
            }

            RangeDayDto dto = RangeDayDto.toDto(rangeDay);

            if (dto.getA_stat().equals("전체")) {
                dtoList.add(0, dto);
            } else {
                dtoList.add(dto);
            }
            responseMap.put(rangeDay.getStatDay(), dtoList);
        }
        return responseMap;
    }

    @Transactional
    public void insertRangeDay(String month) { // DB 에 값 채우기
        List<RangeDay> rangeDayList = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            StringBuilder sb = new StringBuilder();
            String day = String.valueOf(i);

            if (i < 10) {
                day = "0" + day;
            }

            sb.append(month);
            sb.append("-");
            sb.append(day);

            RangeDay totalRangeDay = RangeDay.setDefault(sb.toString(), "A");
            rangeDayList.add(totalRangeDay);

            for (String aStat : A_STAT_ARRAY) {
                rangeDayList.add(RangeDay.setDefault(sb.toString(), aStat));
            }

            createRangeDay(sb.toString(), rangeDayList, totalRangeDay);
        }

        for (RangeDay rangeDay : rangeDayList) {
            rangeDay.calculateFStat();
            rangeDay.calculateGStat();
            rangeDay.roundDFG();

            rangeDayMapper.insertRangeDay(rangeDay);
        }
    }

    private void createRangeDay(String day, List<RangeDay> rangeDayList, RangeDay totalRangeDay) {
        double randomValue = 0;

        for (int i = 0; i < 1000000; i++) {
            randomValue = generateRandomValue();

            RangeDay rangeDay = findRangeDay(day, findAStatRange(randomValue), rangeDayList);
            rangeDay.addValue(randomValue);
            totalRangeDay.addValue(randomValue);
        }
    }

    private double generateRandomValue() {
        Random random = new Random();
        return 0 + (800000.0 - 0.0) * random.nextDouble();
    }

    private String findAStatRange(double value) {
        for (int i = 0; i < AVERAGE_VALUE_ARRAY.length; i++) {
            if (value <= AVERAGE_VALUE_ARRAY[i]) {
                return A_STAT_ARRAY[i];
            }
        }
        return null;
    }

    private RangeDay findRangeDay(String date, String aStat, List<RangeDay> rangeDayList) {
        for (RangeDay rangeDay : rangeDayList) {
            if (rangeDay.getStatDay().equals(date) && rangeDay.getAStat().equals(aStat)) {
                return rangeDay;
            }
        }
        return null;
    }

    public void exportExcel(String startDate, String endDate, String dcb, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("일별 통계");
        sheet.createRow(0);
        XSSFRow headerRow = sheet.createRow(1);
        RangeDayField[] rangeDayFields = RangeDayField.values();
        Field[] rangeDayDtoFields = RangeDayDto.class.getDeclaredFields();

        for (int i = 0; i < rangeDayFields.length; i++) {
            headerRow.createCell(i).setCellValue(rangeDayFields[i].getDescription());
        }

        int rowIndex = 2;
        String lastStatDay = null;
        int startMergeRowIndex = -1;
        Map<String, List<RangeDayDto>> rangeDayMap = getRangeDayList(startDate, endDate, dcb);
        for (Map.Entry<String, List<RangeDayDto>> entry : rangeDayMap.entrySet()) {
            List<RangeDayDto> rangeDayDtoList = entry.getValue();
            for (RangeDayDto dto : rangeDayDtoList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                if (dto.getStatDay().equals(lastStatDay)) {
                    row.createCell(0).setCellValue("");
                } else {
                    if (startMergeRowIndex != -1) {
                        sheet.addMergedRegion(new CellRangeAddress(startMergeRowIndex, rowIndex - 2, 0, 0));
                    }
                    startMergeRowIndex = rowIndex - 1;
                    row.createCell(0).setCellValue(dto.getStatDay());
                    lastStatDay = dto.getStatDay();
                }
                for (int i = 1; i < rangeDayFields.length; i++) {
                    Field field = rangeDayDtoFields[i];
                    field.setAccessible(true);
                    Object value = field.get(dto);
                    if (value != null) {
                        if (value instanceof Double) {
                            row.createCell(i).setCellValue((Double) value);
                        } else if (value instanceof String) {
                            row.createCell(i).setCellValue((String) value);
                        } else {
                            row.createCell(i).setCellValue(value.toString());
                        }
                    } else {
                        row.createCell(i).setCellValue("");
                    }
                }
            }
        }
        if (startMergeRowIndex != -1) {
            sheet.addMergedRegion(new CellRangeAddress(startMergeRowIndex, rowIndex - 1, 0, 0));
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=RangeMonth.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
