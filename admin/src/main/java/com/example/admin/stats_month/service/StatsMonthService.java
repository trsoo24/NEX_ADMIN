package com.example.admin.stats_month.service;

import com.example.admin.stats_month.dto.StatsMonth;
import com.example.admin.stats_month.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsMonthService {
    private final StatsMonthMapper statsMonthMapper;

    // 통합 ADMIN 월별 결제 현황 데이터 수집
    public List<StatsMonth> getStatsMonthList() {
        log.info("getStatsMonthList");

        List<StatsMonth> statsMonthList = new ArrayList<>();

        try {
            Map<String, Object> requestMap = new HashMap<>();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1); // 하루 전으로 설정

            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String month = new SimpleDateFormat("MM").format(cal.getTime());

            requestMap.put("year", year);
            requestMap.put("month", month);

            statsMonthList = statsMonthMapper.getStatsMonthList(requestMap);

            if (!statsMonthList.isEmpty()) {
                // 내부 비율 계산
                calculateStatDay(statsMonthList);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return statsMonthList;
    }

    private void calculateStatDay(List<StatsMonth> statsMonthList) {
        for (StatsMonth statsMonth : statsMonthList) {
            statsMonth.calculateStat();
        }
    }
}
