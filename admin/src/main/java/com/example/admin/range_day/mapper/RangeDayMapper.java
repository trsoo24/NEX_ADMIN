package com.example.admin.range_day.mapper;

import com.example.admin.range_day.dto.GetRangeDayDto;
import com.example.admin.range_day.dto.RangeDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeDayMapper {
    List<RangeDay> getRangeDay(GetRangeDayDto dto);
}
