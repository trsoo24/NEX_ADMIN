package com.example.admin.service.payment;

import com.example.admin.domain.dto.payment.MonthPaymentDto;
import com.example.admin.domain.dto.payment.field.MonthPaymentField;
import com.example.admin.domain.entity.payment.MonthPayment;
import com.example.admin.repository.mapper.monthpayment.*;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthPaymentService {
    private final AdcbMonthPaymentMapper adcbMonthPaymentMapper;
    private final GdcbMonthPaymentMapper gdcbMonthPaymentMapper;
    private final MdcbMonthPaymentMapper mdcbMonthPaymentMapper;
    private final MsdcbMonthPaymentMapper msdcbMonthPaymentMapper;
    private final NdcbMonthPaymentMapper ndcbMonthPaymentMapper;
    private final PdcbMonthPaymentMapper pdcbMonthPaymentMapper;
    private final SdcbMonthPaymentMapper sdcbMonthPaymentMapper;

    public Map<String, List<MonthPayment>> getMonthPayment(List<String> dcbs, String year) {
        Map<String, List<MonthPayment>> responseMap = new LinkedHashMap<>();
        if (dcbs.size() == 1) {
            List<MonthPayment> monthPaymentList = getMonthPaymentList(dcbs.get(0), year);
            responseMap.put("total", monthPaymentList);

            return responseMap;
        } else {
            Map<String, List<MonthPayment>> dcbMonthPaymentMap = new HashMap<>();
            List<MonthPayment> totalMonthPaymentList = new ArrayList<>();
            Map<String, MonthPayment> monthPaymentMap = new HashMap<>();

            for (String dcb : dcbs) {
                List<MonthPayment> monthPaymentList = getMonthPaymentList(dcb, year);
                dcbMonthPaymentMap.put(dcb, monthPaymentList);
                totalMonthPaymentList.addAll(monthPaymentList);
            }

            for (int i = 0; i < totalMonthPaymentList.size(); i++) {
                MonthPayment monthPayment = totalMonthPaymentList.get(i);
                String date = monthPayment.getStat_month();

                if (monthPaymentMap.containsKey(date)) {
                    MonthPayment containsMonthPayment = monthPaymentMap.get(date);
                    monthPayment.addTotalAmount(containsMonthPayment);
                }

                if (monthPaymentMap.size() >= totalMonthPaymentList.size() - i) {
                    monthPayment.calculateStat();
                }
                monthPaymentMap.put(date, monthPayment);
            }
            List<MonthPayment> responseList = new ArrayList<>(monthPaymentMap.values());
            responseList.sort(Comparator.comparing(monthPayment -> YearMonth.parse(monthPayment.getStat_month())));

            responseMap.put("total", responseList);
            responseMap.putAll(dcbMonthPaymentMap);

            return responseMap;
        }
    }

    public Map<String, Map<String, List<Object>>> getMonthPaymentDtoForm(String date, List<String> dcbs) {
        log.info("MonthPayment 조회 API");
        Map<String, MonthPayment> valueMap = new LinkedHashMap<>();
        Map<String, Map<String, List<Object>>> responseMap = new LinkedHashMap<>();

        Map<String, List<MonthPayment>> monthPaymentMap = getMonthPayment(dcbs, date);
        dcbs.add(0, "total");

        for (String dcb : dcbs) {
            List<MonthPayment> monthPaymentList = monthPaymentMap.get(dcb);
            Map<String, List<Object>> dtoMap = new LinkedHashMap<>();
            MonthPayment total = MonthPayment.toTotal("TOTAL");

            calculateMap(valueMap, monthPaymentList, total);
            List<MonthPaymentDto> monthPaymentDtoList = generateDtoList(valueMap, total);

            addDtoMap(dtoMap, monthPaymentDtoList);
            responseMap.put(dcb, dtoMap);
        }

        return responseMap;
    }

    private List<MonthPayment> getMonthPaymentList(String dcb, String year) {
        // TODO DCB 조건값 추가
        String[] dcbArr = dcb.split(",");
        List<MonthPayment> monthPaymentList = new ArrayList<>();

        for (String idx : dcbArr) {
            switch (idx.toLowerCase()) {
                case "adcb" -> monthPaymentList = adcbMonthPaymentMapper.getMonthPaymentList(year);
                case "gdcb" -> monthPaymentList = gdcbMonthPaymentMapper.getMonthPaymentList(year);
                case "mdcb" -> monthPaymentList = mdcbMonthPaymentMapper.getMonthPaymentList(year);
                case "msdcb" -> monthPaymentList = msdcbMonthPaymentMapper.getMonthPaymentList(year);
                case "ndcb" -> monthPaymentList = ndcbMonthPaymentMapper.getMonthPaymentList(year);
                case "pdcb" -> monthPaymentList = pdcbMonthPaymentMapper.getMonthPaymentList(year);
                case "sdcb" -> monthPaymentList = sdcbMonthPaymentMapper.getMonthPaymentList(year);
            }
        }

        return monthPaymentList;
    }

    private List<MonthPaymentDto> generateDtoList(Map<String, MonthPayment> valueMap, MonthPayment total) {
        log.info("generateDtoList");
        List<MonthPaymentDto> monthPaymentDtoList = new ArrayList<>();


        for (String mapKey : valueMap.keySet()) {
            monthPaymentDtoList.add(MonthPaymentDto.toDto(valueMap.get(mapKey)));
        }

        monthPaymentDtoList.add(0, MonthPaymentDto.toTotalDto(total));

        return monthPaymentDtoList;
    }

    private void calculateMap(Map<String, MonthPayment> valueMap, List<MonthPayment> monthPaymentList, MonthPayment total) {

        for (MonthPayment monthPayment : monthPaymentList) {
            if (valueMap.containsKey(monthPayment.getStat_month())) {
                MonthPayment oldMonthPayment = valueMap.get(monthPayment.getStat_month());
                oldMonthPayment.addTotalAmount(monthPayment);

                // 해당 날짜 값 누적
                valueMap.put(monthPayment.getStat_month(), oldMonthPayment);
            } else {
                valueMap.put(monthPayment.getStat_month(), monthPayment);
            }
            total.addTotalAmount(monthPayment);
        }
    }

    private void addDtoMap(Map<String, List<Object>> dtoMap, List<MonthPaymentDto> monthPaymentDtoList) {
        Field[] fields = MonthPaymentDto.class.getDeclaredFields(); // field 이름 기반으로 Array 생성
        for (Field field : fields) {
            field.setAccessible(true);
            List<Object> list = new ArrayList<>();
            dtoMap.put(field.getName(), list);
        }

        for (MonthPaymentDto dto : monthPaymentDtoList) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    String key = field.getName();
                    Object value = field.get(dto);

                    dtoMap.get(key).add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exportMonthPaymentExcel(String year, List<String> dcbs, HttpServletResponse response) throws IOException, IllegalAccessException {
        Map<String, Map<String, List<Object>>> monthPaymentMap = getMonthPaymentDtoForm(year, dcbs);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("월별 통계");

        sheet.createRow(0);

        Field[] fields = MonthPaymentDto.class.getDeclaredFields();
        Field[] excelFields = MonthPaymentField.class.getDeclaredFields();

        for (int j = 0; j < monthPaymentMap.size(); j++) {
            Row row = sheet.createRow(j + 1);
            Field field = excelFields[j];

            if (j > 0 && field.isEnumConstant()) { // 통계 column 값
                row.createCell(0).setCellValue(((MonthPaymentField) field.get(null)).getDescription());
            }

            for (Map<String, List<Object>> paymentMap : monthPaymentMap.values()) {
                List<Object> objects = paymentMap.get(fields[j].getName());

                for (int k = 0; k < objects.size(); k++) {
                    row.createCell(k + 1).setCellValue(objects.get(k).toString());
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=MonthPaymentStats.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    @Transactional
    // DB 초기 테스트 작업용 ( 신경안써도 됨 ! )
    public void insertMonthPayment(String year) {

        for (int i = 1; i <= 12; i++) {
            StringBuilder sb = new StringBuilder();
            String month = String.valueOf(i);

            if (i < 10) {
                month = "0" + month;
            }
            
            sb.append(year);
            sb.append("-");
            sb.append(month);

            adcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            gdcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            mdcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            msdcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            ndcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            pdcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
            sdcbMonthPaymentMapper.insertMonthPayment(createMonthPayment(sb));
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

    private MonthPayment createMonthPayment(StringBuilder sb) {
        Random random = new Random();

        // 9자리 값 생성
        double totalAmount = 1000000 + random.nextDouble(9000000);
        double buyAmount = totalAmount * (0.9 + 0.1 * random.nextDouble());
        double remaining = totalAmount - buyAmount;
        double ratio = random.nextDouble();
        double cancelAmount = remaining * ratio;
        double refundAmount = remaining * (1 - ratio);
        double paymentCount = mathCeil(1000 + random.nextDouble(9000));
        double buyCount = mathCeil(1000 + random.nextDouble(9000));
        double cancelCount = mathCeil(100 + random.nextDouble(900));
        double refundCount = mathCeil(100 + random.nextDouble(900));

        return MonthPayment.builder()
                .stat_month(sb.toString())
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
    }
}
