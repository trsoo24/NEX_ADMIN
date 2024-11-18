package com.example.admin.analysis.mapper;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.dto.MonthAnalysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisStatisticsMapper {
    void insertLogAnalysis(DayAnalysis dayAnalysis);
    void insertMonthAnalysis(MonthAnalysis monthAnalysis);
    List<MonthAnalysis> generateMonthData(Map<String, Object> map);
    List<DayAnalysis> generateAnalysisStatistics(Map<String, Object> map);
    List<DayAnalysis> getAnalysisStatisticsList(Map<String, Object> map);
    List<MonthAnalysis> getAnalysisMonthStatisticsList(Map<String, Object> map);
}
