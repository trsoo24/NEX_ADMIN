package com.example.admin.repository.mapper.item;

import com.example.admin.domain.entity.item.MerchantDayStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsDailyMapper {
    List<MerchantDayStat> getItemDayStats(Map<String, Object> map);
    void insertItemDayStat(MerchantDayStat merchantDayStat);
}
