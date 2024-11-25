package com.example.admin.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class FunctionUtil {
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Value("${stats.last.month.try.cnt}")
    private static String monthTryCnt;

    public String transCtn(String ctn) {
        if (ctn.contains("-")) {
            ctn = ctn.replace("-", "");
        }

        if (ctn.length() == 12) {
            return ctn.replace(ctn.charAt(3) + "", "");
        } else {
            return ctn;
        }
    }

    public String maskingCtn(String ctn) {
        // 숫자만 추출
        String cleanCtn = ctn.replaceAll("[^0-9]", "");

        if (cleanCtn.length() == 10) { // 010-123-4567 형식
            // 원래 하이픈이 있었는지 확인하고, 하이픈을 추가한 마스킹 처리
            return ctn.contains("-") ? cleanCtn.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-***-$3")
                    : cleanCtn.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1***$3");
        } else if (cleanCtn.length() == 11) { // 010-1234-5678 형식
            return ctn.contains("-") ? cleanCtn.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-****-$3")
                    : cleanCtn.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1****$3");
        } else if (cleanCtn.length() == 12) { // 0100-1234-5678 형식
            return ctn.contains("-")
                    ? cleanCtn.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1-****-$3")
                    : cleanCtn.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1****$3");
        }
        return ctn;
    }

    public static String monthToStartDate(String startDate) {
        YearMonth yearMonth = YearMonth.parse(startDate, monthFormatter);

        return yearMonth.atDay(1).toString();
    }

    public static String monthToEndDate(String endDate) {
        YearMonth yearMonth = YearMonth.parse(endDate, monthFormatter);

        return yearMonth.atEndOfMonth().toString();
    }

    public static String toYYYYmmDD(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // "YYYYMMDD" 형식

        return formattedDate;
    }

    public static String monthStartTransToYYYYmmDDHH24MISS(String date) {
        return toYYYYmmDD(monthToStartDate(date)) + "000000";
    }

    public static String montEndTransToYYYYmmDDHH24MISS(String date) {
        return toYYYYmmDD(monthToEndDate(date)) + "235959";
    }

    public static List<String> getlastMonthTryList(){
        List<String> lastMonthList = new ArrayList<>();

        try {
            int cnt = Integer.parseInt(monthTryCnt);

            Calendar cal = Calendar.getInstance();
            if(cnt > 0) {
                cal.add(Calendar.MONTH, -1);
                for(int i=0; i<cnt; i++) {
                    cal.add(Calendar.MONTH, -1);
                    String lastMonth = new SimpleDateFormat("yyyyMM").format(cal.getTime());
                    lastMonthList.add(lastMonth + "," + lastMonth + "01," + lastMonth + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return lastMonthList;
    }

    public static String yearOfPreviousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");

        return lastMonth.format(yearFormatter);
    }

    public static String previousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        return lastMonth.format(monthFormatter);
    }

    public static String yearOfYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");

        return yesterday.format(yearFormatter);
    }

    public static String monthOfYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        return yesterday.format(monthFormatter);
    }

    public static String yesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("dd");

        return yesterday.format(monthFormatter);
    }
}
