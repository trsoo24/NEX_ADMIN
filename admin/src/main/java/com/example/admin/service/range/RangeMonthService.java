package com.example.admin.service.range;

import com.example.admin.domain.dto.payment.field.MonthPaymentField;
import com.example.admin.domain.dto.range.RangeMonthDto;
import com.example.admin.domain.dto.range.field.RangeMonthField;
import com.example.admin.domain.entity.range.RangeMonth;
import com.example.admin.repository.mapper.range.RangeMonthMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RangeMonthService {
    private final RangeMonthMapper rangeMonthMapper;
    private final String[] A_STAT_ARRAY = {"1", "2", "3", "4", "5", "O"};
    private final Double[] AVERAGE_VALUE_ARRAY = {100000.0, 200000.0, 300000.0, 400000.0, 500000.0, 810000.0};

    public Map<String, List<RangeMonthDto>> getRangeMonthList(String startDate, String endDate, String dcb) throws IllegalAccessException {
        Map<String, String> paramMap = new HashMap<>(); // 요청 쿼리
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        List<RangeMonth> rangeMonthList = rangeMonthMapper.getRangeMonthList(paramMap);
        Map<String ,List<RangeMonthDto>> responseMap = new LinkedHashMap<>();

        for (RangeMonth rangeMonth : rangeMonthList) {
            List<RangeMonthDto> dtoList = new ArrayList<>();
            if (responseMap.containsKey(rangeMonth.getStatMonth())) {
                dtoList = responseMap.get(rangeMonth.getStatMonth());
            }

            RangeMonthDto dto = RangeMonthDto.toDto(rangeMonth);

            if (dto.getA_stat().equals("전체")) {
                dtoList.add(0, dto);
            } else {
                dtoList.add(dto);
            }
            responseMap.put(rangeMonth.getStatMonth(), dtoList);
        }
        return responseMap;
    }

    @Transactional
    public void insertRangeMonth(String year) { // DB 에 값 채우기
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

            RangeMonth totalRangeMonth = RangeMonth.setDefault(sb.toString(), "A");
            rangeMonthList.add(totalRangeMonth);

            for (String aStat : A_STAT_ARRAY) {
                rangeMonthList.add(RangeMonth.setDefault(sb.toString(), aStat));
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
            if (rangeMonth.getStatMonth().equals(year) && rangeMonth.getAStat().equals(aStat)) {
                return rangeMonth;
            }
        }
        return null;
    }

    public void exportExcel(String startDate, String endDate, String dcb, HttpServletResponse response) throws IllegalAccessException, IOException {
        Map<String, List<RangeMonthDto>> rangeMonthMap = getRangeMonthList(startDate, endDate, dcb);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("월별 통계");

        sheet.createRow(0);

        Field[] fields = RangeMonthDto.class.getDeclaredFields();
        Field[] excelFields = RangeMonthField.class.getDeclaredFields();

        for (int j = 0; j < rangeMonthMap.size(); j++) {
            Row row = sheet.createRow(j + 1);
            Field field = excelFields[j];

            if (j > 0 && field.isEnumConstant()) { // 통계 column 값
                row.createCell(0).setCellValue(((RangeMonthField) field.get(null)).getDescription());
            }


            for (Map.Entry<String, List<RangeMonthDto>> entry : rangeMonthMap.entrySet()) {
                List<RangeMonthDto> objects = rangeMonthMap.get(entry.getKey());

                for (int k = 0; k < objects.size(); k++) {
                    RangeMonthDto rangeMonthDto = objects.get(k);
                }
            }

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
