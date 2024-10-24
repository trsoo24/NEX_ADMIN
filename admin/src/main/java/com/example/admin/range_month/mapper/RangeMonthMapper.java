package com.example.admin.range_month.mapper;

import com.example.admin.range_month.dto.RangeMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeMonthMapper {
    void insertRangeMonth(RangeMonth rangeMonth);
    List<RangeMonth> getRangeMonthList(Map<String, String> map);
    List<RangeMonth> getRangeMonthScheduleList(Map<String, String> map);
}
