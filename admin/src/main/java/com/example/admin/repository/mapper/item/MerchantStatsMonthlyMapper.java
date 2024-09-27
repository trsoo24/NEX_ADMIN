package com.example.admin.repository.mapper.item;

import com.example.admin.domain.entity.item.MerchantMonthStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsMonthlyMapper {
    List<MerchantMonthStat> getItemMonthStats(Map<String, Object> map);

    void insertItemMonthStat(MerchantMonthStat merchantMonthStat);
}
