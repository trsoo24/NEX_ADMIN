package com.example.admin.range_day.service;

import com.example.admin.range_day.dto.RangeDayDto;
import com.example.admin.range_day.dto.field.RangeDayField;
import com.example.admin.range_day.dto.RangeDay;
import com.example.admin.range_day.mapper.RangeDayMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
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

    public List<RangeDay> getRangeDay(String day, List<String> dcbs) {
        Map<String, Object> paramMap = new HashMap<>(); // 요청 쿼리
        List<RangeDay> rangeDayList = new ArrayList<>();
        paramMap.put("day", day);
        for (String dcb : dcbs) {
            paramMap.put("dcb", dcb);
            List<RangeDay> DCBRangeDayList = rangeDayMapper.getRangeDayScheduleList(paramMap);
            // list 정렬
            sortRangeDayList(DCBRangeDayList);

            rangeDayList.addAll(DCBRangeDayList);
        }

        List<RangeDay> responseList = new ArrayList<>(generateDCBTotalList(rangeDayList));

        if(dcbs.size() == 1) return responseList;

        responseList.addAll(rangeDayList);

        return responseList;
    }

    private List<RangeDay> generateDCBTotalList(List<RangeDay> rangeDayList) {
        Map<String, RangeDay> totalMap = new HashMap<>();

        for (RangeDay rangeDay : rangeDayList) {
            String key = rangeDay.getStat_day() + rangeDay.getA_stat();

            if (totalMap.containsKey(key)) {
                totalMap.get(key).addTotalValue(rangeDay);
            } else {
                // 초기 Total 값 생성
                RangeDay totalRangeDay = RangeDay.setDefault(rangeDay.getStat_day(), rangeDay.getA_stat(), "total");
                totalRangeDay.addTotalValue(rangeDay);

                totalMap.put(key, totalRangeDay);
            }
        }

        List<RangeDay> responseList = new ArrayList<>(totalMap.values());

        // Map 에서 나와서 정렬
        sortRangeDayList(responseList);

        return responseList;
    }

    private Map<String, List<RangeDay>> getRangeDayList(String startDate, String endDate, List<String> dcbs) {
        Map<String, Object> paramMap = new HashMap<>(); // 요청 쿼리
        Map<String, List<RangeDay>> rangeDayMap = new LinkedHashMap<>(); // DCB 별 RangeDay 저장
        List<RangeDay> rangeDayList = new ArrayList<>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        for (String dcb : dcbs) {
            paramMap.put("dcb", dcb);
            List<RangeDay> DCBRangeDayList = rangeDayMapper.getRangeDayList(paramMap);
            // list 정렬
            sortRangeDayList(DCBRangeDayList);
            rangeDayMap.put(dcb, DCBRangeDayList);

            rangeDayList.addAll(DCBRangeDayList);
        }

        Map<String, List<RangeDay>> responseMap = new LinkedHashMap<>();

        List<RangeDay> totalList = new ArrayList<>(generateDCBTotalList(rangeDayList));
        responseMap.put("total", totalList);

        if(dcbs.size() == 1) return responseMap;

        responseMap.putAll(rangeDayMap); // 전체 DCB 추가 저장

        return responseMap;
    }

    public Map<String, List<RangeDayDto>> getRangeDayMap(String startDate, String endDate, List<String> dcbs) throws IllegalAccessException {
        Map<String ,List<RangeDayDto>> responseMap = new LinkedHashMap<>();

        Map<String, List<RangeDay>> rangeDayMap = getRangeDayList(startDate, endDate, dcbs);

        for (List<RangeDay> rangeDayList : rangeDayMap.values()) {
            for (RangeDay rangeDay : rangeDayList) {
                List<RangeDayDto> dtoList = new ArrayList<>();
                if (responseMap.containsKey(rangeDay.getDcb())) {
                    dtoList = responseMap.get(rangeDay.getDcb());
                }

                dtoList.add(RangeDayDto.toDto(rangeDay));

                responseMap.put(rangeDay.getDcb(), dtoList);
            }
        }
        return responseMap;
    }

    @Transactional
    public void insertRangeDay(String month, String dcb) { // DB 에 값 채우기
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
            dcb = dcb.toUpperCase();

            RangeDay totalRangeDay = RangeDay.setDefault(sb.toString(), "A", dcb);
            rangeDayList.add(totalRangeDay);

            for (String aStat : A_STAT_ARRAY) {
                rangeDayList.add(RangeDay.setDefault(sb.toString(), aStat, dcb));
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
            if (rangeDay.getStat_day().equals(date) && rangeDay.getA_stat().equals(aStat)) {
                return rangeDay;
            }
        }
        return null;
    }

    private void sortRangeDayList(List<RangeDay> rangeDayList) {
        rangeDayList.sort(Comparator
                .comparing(RangeDay::getStat_day) // stat_day로 정렬
                .thenComparingInt(rangeDay -> {
                    switch (rangeDay.getA_stat()) {
                        case "A": return 0;
                        case "1": return 1;
                        case "2": return 2;
                        case "3": return 3;
                        case "4": return 4;
                        case "5": return 5;
                        case "O": return 6;
                        default: return Integer.MAX_VALUE; // 예상치 못한 값에 대한 처리
                    }
                })
        );
    }

    public void exportExcel(String startDate, String endDate, List<String> dcbs, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("일별 통계");

        RangeDayField[] rangeDayFields = RangeDayField.values();
        Field[] rangeDayDtoFields = RangeDayDto.class.getDeclaredFields();

        int rowIndex = 0;
        int startMergeRowIndex = - 1;
        String lastStatDay = null;
        Map<String, List<RangeDayDto>> rangeDayMap = getRangeDayMap(startDate, endDate, dcbs);
        XSSFCellStyle middle = workbook.createCellStyle();
        middle.setAlignment(HorizontalAlignment.CENTER);

        for (Map.Entry<String, List<RangeDayDto>> entry : rangeDayMap.entrySet()) {
            String key = entry.getKey();
            List<RangeDayDto> rangeDayDtoList = entry.getValue();

            // Map의 key 병합 처리
            XSSFCell keyCell = sheet.createRow(rowIndex).createCell(0);
            keyCell.setCellValue(key);
            keyCell.setCellStyle(middle); // 중앙 정렬

            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, rangeDayFields.length - 1));

            rowIndex += 2;

            XSSFRow headerRow = sheet.createRow(rowIndex);

            for (int i = 0; i < rangeDayFields.length; i++) {
                headerRow.createCell(i).setCellValue(rangeDayFields[i].getDescription());
            }

            for (RangeDayDto dto : rangeDayDtoList) {
                XSSFRow row = sheet.createRow(++rowIndex);

                // 날짜별 병합 처리
                if (dto.getStat_day().equals(lastStatDay)) {
                    row.createCell(0).setCellValue("");
                } else {
                    if (startMergeRowIndex != -1) {
                        sheet.addMergedRegion(new CellRangeAddress(startMergeRowIndex, rowIndex - 1, 0, 0)); // 날짜 병합
                    }
                    startMergeRowIndex = rowIndex;
                    row.createCell(0).setCellValue(dto.getStat_day());
                    lastStatDay = dto.getStat_day();
                }

                // 나머지 필드 값 입력
                for (int i = 1; i < rangeDayFields.length; i++) {
                    Field field = rangeDayDtoFields[i];
                    field.setAccessible(true);
                    Object value = field.get(dto);
                    if (value != null) {
                        if (value instanceof Double) {
                            row.createCell(i).setCellValue((Double) value); // 셀 입력
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
            // 마지막 날짜 병합
            if (startMergeRowIndex != -1) {
                sheet.addMergedRegion(new CellRangeAddress(startMergeRowIndex, rowIndex, 0, 0));
            }
            rowIndex++;
            startMergeRowIndex = -1;
        }


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=RangeDay.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
