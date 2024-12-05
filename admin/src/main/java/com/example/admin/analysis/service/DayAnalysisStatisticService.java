package com.example.admin.analysis.service;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.mapper.AnalysisStatisticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayAnalysisStatisticService {
    private final AnalysisStatisticsMapper analysisStatisticsMapper;

    private Map<String, Object> putYesterdayDateTime(String trxNo, String day) {
        Map<String, Object> requestMap = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.parse(day, formatter);
        String startDate = yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = yesterday.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        log.info("[{}] 요청 = {} 일 통계 분석 데이터 수집 API 실행", trxNo, yesterday);
        return requestMap;
    }

    // 통계 분석 생성 스케줄러
    public List<DayAnalysis> getAnalysisStatisticsList(String day) {
        String trxNo = MDC.get("trxNo");

        Map<String, Object> requestMap = putYesterdayDateTime(trxNo, day);

        List<DayAnalysis> dayAnalysisList = analysisStatisticsMapper.generateAnalysisStatistics(requestMap);

        log.info("[{}] 응답 데이터 = GDCB 일 통계 분석 데이터 {} 건 호출", trxNo, dayAnalysisList.size());

        return dayAnalysisList;
    }
}
