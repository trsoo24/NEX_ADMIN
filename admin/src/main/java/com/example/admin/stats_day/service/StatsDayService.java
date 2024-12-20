package com.example.admin.stats_day.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.stats_day.dto.GetStatDayDto;
import com.example.admin.stats_day.dto.StatsDay;
import com.example.admin.stats_day.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsDayService {
    private final StatsDayMapper statsDayMapper;

    // 통합 ADMIN 일별 결제 현황 데이터 수집
    public List<StatsDay> getStatsDayList(String year, String month, String day) {
        String trxNo = MDC.get("trxNo");

        List<StatsDay> statsDayList = new ArrayList<>();

        try {
            GetStatDayDto dto = new GetStatDayDto(year, month, day, "B", "C", "R");

            log.info("[{}] 요청 = {} 일별 결제 현황 종합", trxNo, year + "-" + month + "-" + day);
            statsDayList = statsDayMapper.getStatsDayList(dto);

            log.info("[{}] 응답 = 일별 결제 현황 {} 건 종합 완료", trxNo, statsDayList.size());

            if (!statsDayList.isEmpty()) {
                // 내부 비율 계산
                calculateStatDay(statsDayList);
            }
        } catch (Exception e) {
            log.info("[{}] 응답 = 일별 결제 현황 종합 실패", trxNo);

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
