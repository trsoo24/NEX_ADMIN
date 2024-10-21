package com.example.admin.repository.mapper.analysis;

import com.example.admin.domain.entity.analysis.ApiLogs;
import com.example.admin.domain.entity.analysis.AnalysisStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisStatisticsMapper {
    void insertApiLogs(ApiLogs apiLogs);
    void insertLogAnalysis(AnalysisStatistics analysisStatistics);
    List<AnalysisStatistics> generateAnalysisStatistics(Map<String, Object> map);
}
