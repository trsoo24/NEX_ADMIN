package com.example.admin.repository.mapper.range;

import com.example.admin.domain.entity.range.RangeMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeMonthMapper {
    void insertRangeMonth(RangeMonth rangeMonth);
    List<RangeMonth> getRangeMonthList(Map<String, String> map);
    List<RangeMonth> getRangeMonthScheduleList(Map<String, String> map);
}
