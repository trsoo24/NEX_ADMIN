package com.example.admin.repository.mapper.item;

import com.example.admin.domain.entity.item.ItemStatsDaily;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemStatsDailyMapper {
    void insertItemStatDaily(ItemStatsDaily itemStatsDaily);

    List<ItemStatsDaily> getItemDayStats(Map<String ,Object> map);
}
