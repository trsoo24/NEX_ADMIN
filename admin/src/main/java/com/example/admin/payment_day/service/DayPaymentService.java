package com.example.admin.payment_day.service;

import com.example.admin.common.config.filter.CookieUtil;
import com.example.admin.common.config.filter.JwtTokenProvider;
import com.example.admin.payment_day.dto.DayPaymentDto;
import com.example.admin.payment_day.dto.field.DayPaymentField;
import com.example.admin.payment_day.dto.DayPayment;
import com.example.admin.payment_day.mapper.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayPaymentService {
    private final DayPaymentMapper adcbDayPaymentMapper;
    private final GdcbDayPaymentMapper gdcbDayPaymentMapper;
    private final MdcbDayPaymentMapper mdcbDayPaymentMapper;
    private final MSdcbDayPaymentMapper msdcbDayPaymentMapper;
    private final NdcbDayPaymentMapper ndcbDayPaymentMapper;
    private final PdcbDayPaymentMapper pdcbDayPaymentMapper;
    private final SdcbDayPaymentMapper sdcbDayPaymentMapper;
    private final CookieUtil cookieUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, List<DayPayment>> getDayPayment(List<String> dcbs, String month) {
        Map<String, List<DayPayment>> dcbDayPaymentMap = new LinkedHashMap<>();
        Map<String, List<DayPayment>> responseMap = new LinkedHashMap<>();

        if (dcbs.size() == 1) {
            String dcb = dcbs.get(0);
            responseMap.put("total", getDayPaymentList(dcb, month));

            return responseMap;
        } else {
            List<DayPayment> totalDayPaymentList = new ArrayList<>();
            Map<String, DayPayment> dayPaymentMap = new HashMap<>();

            for (String dcb : dcbs) {
                List<DayPayment> dayPaymentList = getDayPaymentList(dcb, month);
                dcbDayPaymentMap.put(dcb, dayPaymentList);
                totalDayPaymentList.addAll(dayPaymentList);
            }

            for (int i = 0; i < totalDayPaymentList.size(); i++) {
                DayPayment dayPayment = DayPayment.copy(totalDayPaymentList.get(i));
                String date = dayPayment.getStat_day();

                if(dayPaymentMap.containsKey(date)) {
                    DayPayment containsDayPayment = dayPaymentMap.get(date);
                    dayPayment.addTotalAmount(containsDayPayment);
                }

                if (dayPaymentMap.size() >= totalDayPaymentList.size() - i) { // 마지막 list 도는 순서일 때 평균값 계산
                    dayPayment.calculateStat();
                }

                dayPaymentMap.put(date, dayPayment);
            }

            List<DayPayment> totalValueList = new ArrayList<>(dayPaymentMap.values());
            totalValueList.sort(Comparator.comparing(dayPayment -> LocalDate.parse(dayPayment.getStat_day())));

            responseMap.put("total", totalValueList);
            responseMap.putAll(dcbDayPaymentMap);

            return responseMap;
        }
    }

    public Map<String, Map<String, List<Object>>> getDayPaymentDtoForm(List<String> dcbs, String month) {
       Map<String, DayPayment> valueMap = new LinkedHashMap<>();
       Map<String, Map<String, List<Object>>> responseMap = new LinkedHashMap<>();


       Map<String, List<DayPayment>> dayPaymentMap = getDayPayment(dcbs, month);
        dcbs.add(0, "total");

       for (String dcb : dcbs) {
           Map<String, List<Object>> dtoMap = new LinkedHashMap<>();
           DayPayment total = DayPayment.toTotal("TOTAL");

           List<DayPayment> dayPaymentList = dayPaymentMap.get(dcb);
           calculateMap(valueMap, dayPaymentList, total);

           List<DayPaymentDto> dayPaymentDtoList = generateDtoList(valueMap, total);

           addDtoMap(dtoMap, dayPaymentDtoList);

           responseMap.put(dcb, dtoMap);

           if (dcbs.size() == 2) {
               return responseMap;
           }
       }

       return responseMap;
    }

    private List<DayPayment> getDayPaymentList(String dcb, String month) {
        log.info("getDayPaymentList");

        List<DayPayment> dayPaymentList = new ArrayList<>();

        switch (dcb.toLowerCase()) {
            case "adcb" -> dayPaymentList = adcbDayPaymentMapper.getDayPaymentList(month);
            case "gdcb" -> dayPaymentList = gdcbDayPaymentMapper.getDayPaymentList(month);
            case "mdcb" -> dayPaymentList = mdcbDayPaymentMapper.getDayPaymentList(month);
            case "msdcb" -> dayPaymentList = msdcbDayPaymentMapper.getDayPaymentList(month);
            case "ndcb" -> dayPaymentList = ndcbDayPaymentMapper.getDayPaymentList(month);
            case "pdcb" -> dayPaymentList = pdcbDayPaymentMapper.getDayPaymentList(month);
            case "sdcb" -> dayPaymentList = sdcbDayPaymentMapper.getDayPaymentList(month);
        }

        return dayPaymentList;
    }

    private List<DayPaymentDto> generateDtoList(Map<String, DayPayment> valueMap, DayPayment total) {
        log.info("generateDtoList");
        List<DayPaymentDto> dayPaymentDtoList = new ArrayList<>();


        for (String mapKey : valueMap.keySet()) {
            dayPaymentDtoList.add(DayPaymentDto.toDto(valueMap.get(mapKey)));
        }

        dayPaymentDtoList.add(0, DayPaymentDto.toTotalDto(total));

        return dayPaymentDtoList;
    }

    private void calculateMap(Map<String, DayPayment> valueMap, List<DayPayment> dayPaymentList, DayPayment total) {

        for (DayPayment dayPayment : dayPaymentList) {
            if (valueMap.containsKey(dayPayment.getStat_day())) { // 이미 해당 월 값이 존재할 때
                DayPayment oldDayPayment = valueMap.get(dayPayment.getStat_day());
                oldDayPayment.addTotalAmount(dayPayment);

                // 해당 날짜 값 누적
                valueMap.put(dayPayment.getStat_day(), oldDayPayment);
            } else {
                valueMap.put(dayPayment.getStat_day(), dayPayment);
            }
            total.addTotalAmount(dayPayment);
        }
    }

    private void addDtoMap(Map<String, List<Object>> dtoMap, List<DayPaymentDto> dayPaymentDtoList) {
        Field[] fields = DayPaymentDto.class.getDeclaredFields(); // field 이름 기반으로 Array 생성
        for (Field field : fields) {
            field.setAccessible(true);
            List<Object> list = new ArrayList<>();
            dtoMap.put(field.getName(), list);
        }

        for (DayPaymentDto dto : dayPaymentDtoList) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    String key = field.getName();
                    Object value = field.get(dto);

//                    int day = dto.getStatDay().equals("TOTAL") ? 0 : Integer.parseInt(dto.getStatDay().substring(dto.getStatDay().length() - 2));

                    dtoMap.get(key).add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Transactional
    // DB 초기 테스트 작업용 ( 신경안써도 됨 ! )
    public void insertDayPayment(String date) {

        for (int i = 1; i <= 30; i++) {
            StringBuilder sb = new StringBuilder();
            String day = "";
            if (i < 10) {
                day = "0" + i;
            } else {
                day = String.valueOf(i);
            }

            sb.append(date);
            sb.append("-");
            sb.append(day);

            Random random = new Random();

            // 9자리 값 생성
            double totalAmount = 100000 + random.nextDouble(900000);
            double buyAmount = totalAmount * (0.9 + 0.1 * random.nextDouble());
            double remaining = totalAmount - buyAmount;
            double ratio = random.nextDouble();
            double cancelAmount = remaining * ratio;
            double refundAmount = remaining * (1 - ratio);
            double paymentCount = mathCeil(100 + random.nextDouble(900));
            double buyCount = mathCeil(100 + random.nextDouble(900));
            double cancelCount = mathCeil(10 + random.nextDouble(90));
            double refundCount = mathCeil(10 + random.nextDouble(90));

            DayPayment dayPayment = DayPayment.builder()
                    .stat_day(sb.toString())
                    .a_stat(mathCeil(totalAmount))
                    .b_stat(mathCeil(buyAmount))
                    .c_stat(calculatePercent(buyCount, totalAmount))
                    .d_stat(mathCeil(cancelAmount))
                    .e_stat(calculatePercent(cancelAmount, totalAmount))
                    .f_stat(mathCeil(refundAmount))
                    .g_stat(calculatePercent(refundAmount, totalAmount))
                    .h_stat(paymentCount)
                    .i_stat(buyCount)
                    .j_stat(calculatePercent(buyCount, paymentCount))
                    .k_stat(cancelCount)
                    .l_stat(calculatePercent(cancelCount, paymentCount))
                    .m_stat(refundCount)
                    .n_stat(calculatePercent(refundAmount, paymentCount))
                    .p_stat(calculateAverage(buyAmount, buyCount))
                    .r_stat(calculateAverage(cancelAmount, cancelCount))
                    .t_stat(calculateAverage(refundAmount, refundCount))
                    .build();


            adcbDayPaymentMapper.insertDayPayment(dayPayment);
            gdcbDayPaymentMapper.insertDayPayment(dayPayment);
            mdcbDayPaymentMapper.insertDayPayment(dayPayment);
            msdcbDayPaymentMapper.insertDayPayment(dayPayment);
            ndcbDayPaymentMapper.insertDayPayment(dayPayment);
            pdcbDayPaymentMapper.insertDayPayment(dayPayment);
            sdcbDayPaymentMapper.insertDayPayment(dayPayment);
        }
    }

    private String calculatePercent(double numerator, double denominator) {
        return Math.round(numerator / denominator * 100 * 10) / 10.0 + "%";
    }

    private double calculateAverage(double numerator, double denominator) {
        return Math.round(numerator / denominator * 100 * 10) / 10.0;
    }

    private double mathCeil(double value) {
        return Math.ceil(value);
    }


    public void exportDayPaymentExcel(String month, List<String> dcbs, HttpServletResponse response) throws IOException {
        Map<String, Map<String, List<Object>>> dayPaymentMap = getDayPaymentDtoForm(dcbs, month);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("일별 통계");

        int rowIdx = 1;

        for (String outerKey : dayPaymentMap.keySet()) {
            Map<String, List<Object>> innerMap = dayPaymentMap.get(outerKey);

            Row outerRow = sheet.createRow(rowIdx);
            Cell outerCell = outerRow.createCell(0);
            outerCell.setCellValue(outerKey);  // total 등 입력

            int innerRowCount = 0;

            for (String innerKey : innerMap.keySet()) {
                List<Object> values = innerMap.get(innerKey);

                Row dateRow = sheet.createRow(rowIdx + 1 + innerRowCount);
                Cell dateCell = dateRow.createCell(1);
                dateCell.setCellValue(innerKey);  // 날짜 입력

                for (int i = 0; i < values.size(); i++) {
                    Cell valueCell = dateRow.createCell(i + 2);
                    valueCell.setCellValue(values.get(i).toString());
                }

                innerRowCount++;
            }
            // TOTAL , DCB 별 병합
            sheet.addMergedRegion(new CellRangeAddress(outerCell.getRowIndex(), outerCell.getRowIndex() + innerRowCount, 0, 0));

            rowIdx += 1 + innerRowCount;
        }


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=DayPaymentStats.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    public void exportDayPaymentExcel2(HttpServletRequest request, String month, List<String> dcbs, HttpServletResponse response) throws IOException, IllegalAccessException {
        // 날짜 : { value1, value2, ... } 정렬 방식 엑셀
        Map<String, DayPayment> valueMap = new LinkedHashMap<>();
        Map<String, List<DayPayment>> dayPaymentMap = getDayPayment(dcbs, month);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("일별 통계 ver.2");
        sheet.createRow(0);
        XSSFRow headerRow = sheet.createRow(1);

        for (List<DayPayment> dayPaymentList : dayPaymentMap.values()) {
            DayPayment total = DayPayment.toTotal("TOTAL");
            calculateMap(valueMap, dayPaymentList, total);

            List<DayPaymentDto> dayPaymentDtoList = generateDtoList(valueMap, total);

            DayPaymentField[] dayPaymentFields = DayPaymentField.values();
            Field[] fields = DayPaymentDto.class.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                headerRow.createCell(i).setCellValue(dayPaymentFields[i].getDescription());
            }

            int rowIdx = 2;
            for (int j = 0; j < dayPaymentDtoList.size(); j++) {
                DayPaymentDto dayPaymentDto = dayPaymentDtoList.get(j);
                XSSFRow row = sheet.createRow(rowIdx++);
                for (int k = 0; k < fields.length; k++) {
                    Field field = fields[k];
                    field.setAccessible(true);
                    row.createCell(k).setCellValue(String.valueOf(field.get(dayPaymentDto)));
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=DayPaymentStats.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
