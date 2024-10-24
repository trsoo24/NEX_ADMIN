package com.example.admin.item.mapper;

import com.example.admin.item.dto.ItemStatsDaily;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemStatsDailyMapper {
    void insertItemStatDaily(ItemStatsDaily itemStatsDaily);

    List<ItemStatsDaily> getItemDayStats(Map<String ,Object> map);
}
