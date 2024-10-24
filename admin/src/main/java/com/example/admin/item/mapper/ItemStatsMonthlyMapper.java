package com.example.admin.item.mapper;

import com.example.admin.item.dto.ItemStatsMonthly;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemStatsMonthlyMapper {
    void insertItemStatMonthly(ItemStatsMonthly itemStatsMonthly);
    List<ItemStatsMonthly> getItemMonthStats(Map<String, Object> map);
}
