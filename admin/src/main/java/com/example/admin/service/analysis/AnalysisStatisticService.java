package com.example.admin.service.analysis;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.entity.analysis.AnalysisStatistics;
import com.example.admin.repository.mapper.analysis.AnalysisStatisticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisStatisticService {
    private final AnalysisStatisticsMapper analysisStatisticsMapper;
    private final FunctionUtil functionUtil;

    private void putYesterdayDateTime(Map<String, Object> map) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String startDate = yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = yesterday.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        map.put("startDate", startDate);
        map.put("endDate", endDate);
    }

    // 통계 분석 생성 스케줄러
    private List<AnalysisStatistics> generateAnalysisStatistics() {
        Map<String, Object> requestMap = new HashMap<>();
        putYesterdayDateTime(requestMap);

        return analysisStatisticsMapper.generateAnalysisStatistics(requestMap);
    }

    // 스케줄러를 실행하고 통계 분석 값 Insert
    @Transactional
    public void insertAnalysisStaticsData() {
        List<AnalysisStatistics> analysisStatisticsList = generateAnalysisStatistics();

        for (AnalysisStatistics analysisStatistics : analysisStatisticsList) {
            if (analysisStatistics.getCreateDt() != null && analysisStatistics.getDcb() != null) {
                analysisStatisticsMapper.insertLogAnalysis(analysisStatistics);
            }
        }
    }
}
