package com.example.admin.stats_day.mapper;

import com.example.admin.stats_day.dto.StatsDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsDayMapper {
    List<StatsDay> getStatsDayList(Map<String, Object> map);
}
