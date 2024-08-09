package com.example.admin.service.payment;

import com.example.admin.domain.dto.payment.DayPaymentDto;
import com.example.admin.domain.dto.payment.PaymentExcelDto;
import com.example.admin.domain.entity.payment.DayPayment;
import com.example.admin.repository.mapper.daypayment.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayPaymentService {
    private final AdcbDayPaymentMapper adcbDayPaymentMapper;
    private final GdcbDayPaymentMapper gdcbDayPaymentMapper;
    private final MdcbDayPaymentMapper mdcbDayPaymentMapper;
    private final MSdcbDayPaymentMapper msdcbDayPaymentMapper;
    private final NdcbDayPaymentMapper ndcbDayPaymentMapper;
    private final PdcbDayPaymentMapper pdcbDayPaymentMapper;
    private final SdcbDayPaymentMapper sdcbDayPaymentMapper;


    public Map<String, List<Object>> getDayPaymentDtoForm(String date, String dcb) {
       log.info("getDayPaymentDtoForm");
       Map<String, DayPayment> valueMap = new LinkedHashMap<>();
       Map<String, List<Object>> dtoMap = new LinkedHashMap<>();

       DayPayment total = DayPayment.toTotal();
       getDayPaymentList(valueMap, date, dcb, total);

       List<DayPaymentDto> dayPaymentDtoList = generateDtoList(valueMap, total);

       addDtoMap(dtoMap, dayPaymentDtoList);

       return dtoMap;
    }

    private void getDayPaymentList(Map<String, DayPayment> valueMap, String date, String dcb, DayPayment total) {
        log.info("getDayPaymentList");
        // TODO DCB 조건값 추가
        String[] dcbArr = dcb.split(",");

        for (String idx : dcbArr) {
            List<DayPayment> dayPaymentList = new ArrayList<>();
            switch (idx.toLowerCase()) {
                case "adcb" -> dayPaymentList = adcbDayPaymentMapper.getDayPaymentList(date);
                case "gdcb" -> dayPaymentList = gdcbDayPaymentMapper.getDayPaymentList(date);
                case "mdcb" -> dayPaymentList = mdcbDayPaymentMapper.getDayPaymentList(date);
                case "msdcb" -> dayPaymentList = msdcbDayPaymentMapper.getDayPaymentList(date);
                case "ndcb" -> dayPaymentList = ndcbDayPaymentMapper.getDayPaymentList(date);
                case "pdcb" -> dayPaymentList = pdcbDayPaymentMapper.getDayPaymentList(date);
                case "sdcb" -> dayPaymentList = sdcbDayPaymentMapper.getDayPaymentList(date);
            }

            calculateMap(valueMap, dayPaymentList, total);
        }
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
            if (valueMap.containsKey(dayPayment.getStatDay())) { // 이미 해당 월 값이 존재할 때
                DayPayment oldDayPayment = valueMap.get(dayPayment.getStatDay());
                oldDayPayment.addTotalAmount(dayPayment);

                // 해당 날짜 값 누적
                valueMap.put(dayPayment.getStatDay(), oldDayPayment);
            } else {
                valueMap.put(dayPayment.getStatDay(), dayPayment);
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
                    .statDay(sb.toString())
                    .aStat(mathCeil(totalAmount))
                    .bStat(mathCeil(buyAmount))
                    .cStat(calculatePercent(buyCount, totalAmount))
                    .dStat(mathCeil(cancelAmount))
                    .eStat(calculatePercent(cancelAmount, totalAmount))
                    .fStat(mathCeil(refundAmount))
                    .gStat(calculatePercent(refundAmount, totalAmount))
                    .hStat(paymentCount)
                    .iStat(buyCount)
                    .jStat(calculatePercent(buyCount, paymentCount))
                    .kStat(cancelCount)
                    .lStat(calculatePercent(cancelCount, paymentCount))
                    .mStat(refundCount)
                    .nStat(calculatePercent(refundAmount, paymentCount))
                    .pStat(calculateAverage(buyAmount, buyCount))
                    .rStat(calculateAverage(cancelAmount, cancelCount))
                    .tStat(calculateAverage(refundAmount, refundCount))
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


    public void exportDayPaymentExcel(String month, String dcb, HttpServletResponse response) throws IOException {
        Map<String, List<Object>> dayPaymentMap = getDayPaymentDtoForm(month, dcb);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("일별 통계");

        sheet.createRow(0);

        Field[] fields = DayPaymentDto.class.getDeclaredFields();

        for (int j = 0; j < dayPaymentMap.size(); j++) {
            Row row = sheet.createRow(j + 1);

            if (j > 0) { // 통계 column 값
                row.createCell(0).setCellValue(fields[j].getName());
            }

            List<Object> objects = dayPaymentMap.get(fields[j].getName());
            for (int k = 0; k < objects.size(); k++) {
                row.createCell(k + 1).setCellValue(objects.get(k).toString());
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

    public void exportDayPaymentExcel2(PaymentExcelDto dto, HttpServletResponse response) throws IOException {
        Map<String, List<Object>> dayPaymentMap = getDayPaymentDtoForm(dto.getDate(), dto.getDcb());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("일별 통계");

        sheet.createRow(0);

        Field[] fields = DayPaymentDto.class.getDeclaredFields();

        for (int j = 0; j < dayPaymentMap.size(); j++) {
            Row row = sheet.createRow(j + 1);

            if (j > 0) { // 통계 column 값
                row.createCell(0).setCellValue(fields[j].getName());
            }

            List<Object> objects = dayPaymentMap.get(fields[j].getName());
            for (int k = 0; k < objects.size(); k++) {
                row.createCell(k + 1).setCellValue(objects.get(k).toString());
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
