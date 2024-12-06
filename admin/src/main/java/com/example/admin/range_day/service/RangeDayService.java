package com.example.admin.range_day.service;

import com.example.admin.range_day.dto.RangeDay;
import com.example.admin.range_day.mapper.RangeDayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RangeDayService {
    private final RangeDayMapper rangeDayMapper;

    public List<RangeDay> getRangeDay() {
        String trxNo = MDC.get("trxNo");

        Map<String, Object> requestMap = new HashMap<>();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(yesterday);

        requestMap.put("date", date);
        requestMap.put("api_type1", "B");
        requestMap.put("api_type2", "C");
        requestMap.put("api_type3", "R");

        log.info("[{}] 요청 = {} 일자 일 기간별 결제 현황 종합", trxNo, date);

        List<RangeDay> rangeDayList = rangeDayMapper.getRangeDay(requestMap);

        log.info("[{}] 응답 = 일 기간별 결제 현황 {} 건 종합 완료", trxNo, rangeDayList.size());

        // A_STAT = "A" 값 추가
        generateDCBTotalList(rangeDayList);

        return rangeDayList;
    }

    private void generateDCBTotalList(List<RangeDay> rangeDayList) {
        List<RangeDay> totalList = new ArrayList<>();
        String statDay = rangeDayList.get(0).getStat_day();
        RangeDay total = RangeDay.setDefault(statDay, "A");

        for (RangeDay rangeDay : rangeDayList) {
            if (statDay.equals(rangeDay.getStat_day())) {
                total.addTotalValue(rangeDay);
            } else {
                totalList.add(total);
                statDay = rangeDay.getStat_day();
                total = RangeDay.setDefault(statDay, "A");

                total.addTotalValue(rangeDay);
            }
        }

        // A_STAT = "A" 인 값 모두 추가
        rangeDayList.addAll(totalList);

        // 정렬
        sortRangeDayList(rangeDayList);
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
}
