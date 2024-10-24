package com.example.admin.repository.mapper.analysis;

import com.example.admin.domain.entity.analysis.ApiLogs;
import com.example.admin.domain.entity.analysis.DayAnalysis;
import com.example.admin.domain.entity.analysis.MonthAnalysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisStatisticsMapper {
    void insertApiLogs(ApiLogs apiLogs);
    void insertLogAnalysis(DayAnalysis dayAnalysis);
    void insertMonthAnalysis(MonthAnalysis monthAnalysis);
    List<MonthAnalysis> generateMonthData(Map<String, Object> map);
    List<DayAnalysis> generateAnalysisStatistics(Map<String, Object> map);
    List<DayAnalysis> getAnalysisStatisticsList(Map<String, Object> map);
    List<MonthAnalysis> getAnalysisMonthStatisticsList(Map<String, Object> map);
}
