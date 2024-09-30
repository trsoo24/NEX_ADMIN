package com.example.admin.repository.mapper.item;

import com.example.admin.domain.entity.item.ItemStatsMonthly;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemStatsMonthlyMapper {
    void insertItemStatMonthly(ItemStatsMonthly itemStatsMonthly);
    List<ItemStatsMonthly> getItemMonthStats(Map<String, Object> map);
}
