package com.example.admin.range_month.mapper;

import com.example.admin.range_month.dto.RangeMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RangeMonthMapper {
    List<RangeMonth> getRangeMonthScheduleList();
}
