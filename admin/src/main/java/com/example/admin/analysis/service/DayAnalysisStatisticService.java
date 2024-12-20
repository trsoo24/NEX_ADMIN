package com.example.admin.analysis.service;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.dto.GetDayAnalysisDto;
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

    private GetDayAnalysisDto putYesterdayDateTime(String trxNo, String day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.parse(day, formatter);
        String startDate = yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = yesterday.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        GetDayAnalysisDto dto = new GetDayAnalysisDto(startDate, endDate);

        log.info("[{}] 요청 = {} 일 통계 분석 데이터 수집 API 실행", trxNo, yesterday);
        return dto;
    }

    // 통계 분석 생성 스케줄러
    public List<DayAnalysis> getAnalysisStatisticsList(String date) {
        String trxNo = MDC.get("trxNo");

        GetDayAnalysisDto dto = putYesterdayDateTime(trxNo, date);

        List<DayAnalysis> dayAnalysisList = analysisStatisticsMapper.generateAnalysisStatistics(dto);

        log.info("[{}] 응답 데이터 = GDCB 일 통계 분석 데이터 {} 건 호출", trxNo, dayAnalysisList.size());

        return dayAnalysisList;
    }
}
