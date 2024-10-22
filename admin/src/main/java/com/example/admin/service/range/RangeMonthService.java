package com.example.admin.service.range;

import com.example.admin.domain.dto.range.RangeDayDto;
import com.example.admin.domain.dto.range.RangeMonthDto;
import com.example.admin.domain.dto.range.field.RangeMonthField;
import com.example.admin.domain.entity.range.RangeMonth;
import com.example.admin.repository.mapper.range.RangeMonthMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RangeMonthService {
    private final RangeMonthMapper rangeMonthMapper;
    private final String[] A_STAT_ARRAY = {"1", "2", "3", "4", "5", "O"};
    private final Double[] AVERAGE_VALUE_ARRAY = {100000.0, 200000.0, 300000.0, 400000.0, 500000.0, 810000.0};

    public List<RangeMonth> getRangeMonth(String month, List<String> dcbs) {
        Map<String, String> paramMap = new HashMap<>(); // 요청 쿼리
        List<RangeMonth> rangeMonthList = new ArrayList<>();
        paramMap.put("month", month);

        for(String dcb : dcbs) {
            paramMap.put("dcb", dcb);

            List<RangeMonth> DCBRangeMonthList = rangeMonthMapper.getRangeMonthScheduleList(paramMap);
            // list 정렬
            sortRangeMonthList(DCBRangeMonthList);

            rangeMonthList.addAll(DCBRangeMonthList);
        }

        List<RangeMonth> responseList = new ArrayList<>(generateDCBTotalList(rangeMonthList));

        if(dcbs.size() == 1) return responseList;

        responseList.addAll(rangeMonthList);

        return responseList;
    }

    private List<RangeMonth> generateDCBTotalList(List<RangeMonth> rangeMonthList) {
        Map<String, RangeMonth> totalMap = new HashMap<>();

        for (RangeMonth rangeMonth : rangeMonthList) {
            String key = rangeMonth.getStat_month() + rangeMonth.getA_stat();

            if (totalMap.containsKey(key)) {
                totalMap.get(key).addTotalValue(rangeMonth);
            } else {
                // 초기 Total 값 생성
                RangeMonth totalRangeMonth = RangeMonth.setDefault(rangeMonth.getStat_month(), rangeMonth.getA_stat(), "total");
                totalRangeMonth.addTotalValue(rangeMonth);

                totalMap.put(key, totalRangeMonth);
            }
        }

        List<RangeMonth> responseList = new ArrayList<>(totalMap.values());

        // Map 에서 나와서 정렬
        sortRangeMonthList(responseList);

        return responseList;
    }

    private List<RangeMonth> getRangeMonthList(String startDate, String endDate, List<String> dcbs) {
        Map<String, String> paramMap = new HashMap<>(); // 요청 쿼리
        List<RangeMonth> rangeMonthList = new ArrayList<>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        for(String dcb : dcbs) {
            paramMap.put("dcb", dcb);

            List<RangeMonth> DCBRangeMonthList = rangeMonthMapper.getRangeMonthList(paramMap);
            // list 정렬
            sortRangeMonthList(DCBRangeMonthList);

            rangeMonthList.addAll(DCBRangeMonthList);
        }

        List<RangeMonth> responseList = new ArrayList<>(generateDCBTotalList(rangeMonthList));

        // TODO
        if(dcbs.size() == 1) return responseList;

        responseList.addAll(rangeMonthList);

        return responseList;
    }

    public Map<String, List<RangeMonthDto>> getRangeMonthMap(String startDate, String endDate, List<String> dcbs) throws IllegalAccessException {
        Map<String ,List<RangeMonthDto>> responseMap = new LinkedHashMap<>();
        List<RangeMonth> rangeMonthList = getRangeMonthList(startDate, endDate, dcbs);

        for (RangeMonth rangeMonth : rangeMonthList) {
            List<RangeMonthDto> dtoList = new ArrayList<>();
            if (responseMap.containsKey(rangeMonth.getDcb())) {
                dtoList = responseMap.get(rangeMonth.getDcb());
            }

            RangeMonthDto dto = RangeMonthDto.toDto(rangeMonth);

            dtoList.add(dto);
            responseMap.put(rangeMonth.getDcb(), dtoList);
        }
        return responseMap;
    }

    @Transactional
    public void insertRangeMonth(String year, String dcb) { // DB 에 값 채우기
        List<RangeMonth> rangeMonthList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            StringBuilder sb = new StringBuilder();
            String month = String.valueOf(i);

            if (i < 10) {
                month = "0" + month;
            }

            sb.append(year);
            sb.append("-");
            sb.append(month);

            RangeMonth totalRangeMonth = RangeMonth.setDefault(sb.toString(), "A", dcb);
            rangeMonthList.add(totalRangeMonth);

            for (String aStat : A_STAT_ARRAY) {
                rangeMonthList.add(RangeMonth.setDefault(sb.toString(), aStat, dcb));
            }

            createRangeMonth(sb.toString(), rangeMonthList, totalRangeMonth);
        }

        for (RangeMonth rangeMonth : rangeMonthList) {
            rangeMonth.calculateFStat();
            rangeMonth.calculateGStat();
            rangeMonth.roundDFG();

            rangeMonthMapper.insertRangeMonth(rangeMonth);
        }
    }

    private void createRangeMonth(String month, List<RangeMonth> rangeMonthList, RangeMonth totalRangeMonth) {
        double randomValue = 0;

        for (int i = 0; i < 1000000; i++) {
            randomValue = generateRandomValue();

            RangeMonth rangeMonth = findRangeMonth(month, findAStatRange(randomValue), rangeMonthList);
            rangeMonth.addValue(randomValue);
            totalRangeMonth.addValue(randomValue);
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

    private RangeMonth findRangeMonth(String year, String aStat, List<RangeMonth> rangeMonthList) {
        for (RangeMonth rangeMonth : rangeMonthList) {
            if (rangeMonth.getStat_month().equals(year) && rangeMonth.getA_stat().equals(aStat)) {
                return rangeMonth;
            }
        }
        return null;
    }

    private void sortRangeMonthList(List<RangeMonth> rangeMonthList) {
        rangeMonthList.sort(Comparator
                .comparing(RangeMonth::getStat_month) // stat_month 로 정렬
                .thenComparingInt(rangeMonth -> {
                    switch (rangeMonth.getA_stat()) {
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

    public void exportExcel(String startDate, String endDate, List<String> dcbs, HttpServletResponse response) throws IllegalAccessException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("월별 통계");

        RangeMonthField[] rangeMonthFields = RangeMonthField.values();
        Field[] rangeMonthDtoFields = RangeMonthDto.class.getDeclaredFields();


        int rowIndex = 0;
        int startMergeRowIndex = - 1;
        String lastStatMonth = null;
        Map<String, List<RangeMonthDto>> rangeMonthMap = getRangeMonthMap(startDate, endDate, dcbs);
        XSSFCellStyle middle = workbook.createCellStyle();
        middle.setAlignment(HorizontalAlignment.CENTER);

        for (Map.Entry<String, List<RangeMonthDto>> entry : rangeMonthMap.entrySet()) {
            String key = entry.getKey();
            List<RangeMonthDto> rangeDayDtoList = entry.getValue();

            // Map의 key 병합 처리
            XSSFCell keyCell = sheet.createRow(rowIndex).createCell(0);
            keyCell.setCellValue(key);
            keyCell.setCellStyle(middle); // 중앙 정렬

            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, rangeMonthFields.length - 1));

            rowIndex += 2;

            XSSFRow headerRow = sheet.createRow(rowIndex);

            for (int i = 0; i < rangeMonthFields.length; i++) {
                headerRow.createCell(i).setCellValue(rangeMonthFields[i].getDescription());
            }

            for (RangeMonthDto dto : rangeDayDtoList) {
                XSSFRow row = sheet.createRow(++rowIndex);

                // 날짜별 병합 처리
                if (dto.getStat_month().equals(lastStatMonth)) {
                    row.createCell(0).setCellValue("");
                } else {
                    if (startMergeRowIndex != -1) {
                        sheet.addMergedRegion(new CellRangeAddress(startMergeRowIndex, rowIndex - 1, 0, 0)); // 날짜 병합
                    }
                    startMergeRowIndex = rowIndex;
                    row.createCell(0).setCellValue(dto.getStat_month());
                    lastStatMonth = dto.getStat_month();
                }

                // 나머지 필드 값 입력
                for (int i = 1; i < rangeMonthDtoFields.length; i++) {
                    Field field = rangeMonthDtoFields[i];
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
        response.setHeader("Content-Disposition", "attachment; filename=RangeMonth.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
