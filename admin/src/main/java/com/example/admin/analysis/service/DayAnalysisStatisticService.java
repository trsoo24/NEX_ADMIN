package com.example.admin.analysis.service;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.mapper.AnalysisStatisticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayAnalysisStatisticService {
    private final AnalysisStatisticsMapper analysisStatisticsMapper;

    private Map<String, Object> putYesterdayDateTime(String day) {
        Map<String, Object> requestMap = new HashMap<>();
//        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.parse(day, formatter);
        String startDate = yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = yesterday.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        return requestMap;
    }

    // 통계 분석 생성 스케줄러
    public List<DayAnalysis> getAnalysisStatisticsList(String day) {
        Map<String, Object> requestMap = putYesterdayDateTime(day);

        return analysisStatisticsMapper.generateAnalysisStatistics(requestMap);
    }
}
