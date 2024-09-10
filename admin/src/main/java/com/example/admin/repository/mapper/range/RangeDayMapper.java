package com.example.admin.repository.mapper.range;

import com.example.admin.domain.entity.range.RangeDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeDayMapper {
    void insertRangeDay(RangeDay rangeDay);

    List<RangeDay> getRangeDayList(Map<String, String> paramMap);

    List<RangeDay> getRangeDayScheduleList(Map<String, String> map);
}
