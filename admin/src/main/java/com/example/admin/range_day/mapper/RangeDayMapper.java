package com.example.admin.range_day.mapper;

import com.example.admin.range_day.dto.RangeDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeDayMapper {
    void insertRangeDay(RangeDay rangeDay);

    List<RangeDay> getRangeDayList(Map<String, Object> paramMap);

    List<RangeDay> getRangeDayScheduleList(Map<String, Object> map);
}
