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
        List<StatsDay> statsDayList = new ArrayList<>();

        try {
            Map<String, Object> requestMap = new HashMap<>();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1); // 하루 전으로 설정

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String formattedDate = sdf.format(cal.getTime());

            requestMap.put("date", formattedDate);
            requestMap.put("api_type1", "charge");
            requestMap.put("api_type2", "reversal");
            requestMap.put("api_type3", "refund");

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
