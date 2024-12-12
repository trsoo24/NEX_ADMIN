package com.example.admin.analysis.mapper;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.dto.GetDayAnalysisDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisStatisticsMapper {
    List<DayAnalysis> generateAnalysisStatistics(GetDayAnalysisDto dto);
}
