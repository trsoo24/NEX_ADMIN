package com.example.admin.stats_month.mapper;

import com.example.admin.stats_month.dto.StatsMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsMonthMapper {
    List<StatsMonth> getStatsMonthList(Map<String, Object> map);
}
