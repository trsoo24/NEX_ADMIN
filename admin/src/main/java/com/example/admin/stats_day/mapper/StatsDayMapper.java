package com.example.admin.stats_day.mapper;

import com.example.admin.stats_day.dto.GetStatDayDto;
import com.example.admin.stats_day.dto.StatsDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatsDayMapper {
    List<StatsDay> getStatsDayList(GetStatDayDto dto);
}
