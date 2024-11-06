package com.example.admin.stats_day.service;

import com.example.admin.stats_day.dto.StatsDay;
import com.example.admin.stats_day.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsDayService {
    private final StatsDayMapper statsDayMapper;

    // 통합 ADMIN 일별 결제 현황 데이터 수집
    public List<StatsDay> getStatsDayList() {
        log.info("getStatsDayList");

        List<StatsDay> statsDayList = new ArrayList<>();

        try {
            Map<String, Object> requestMap = new HashMap<>();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1); // 하루 전으로 설정

            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String month = new SimpleDateFormat("MM").format(cal.getTime());
            String day = new SimpleDateFormat("dd").format(cal.getTime());

            requestMap.put("year", year);
            requestMap.put("month", month);
            requestMap.put("day", day);

            statsDayList = statsDayMapper.getStatsDayList(requestMap);

            if (!statsDayList.isEmpty()) {
                // 내부 비율 계산
                calculateStatDay(statsDayList);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return statsDayList;
    }

    private void calculateStatDay(List<StatsDay> statsDayList) {
        for (StatsDay statsDay : statsDayList) {
            statsDay.calculateStat();
        }
    }
}
