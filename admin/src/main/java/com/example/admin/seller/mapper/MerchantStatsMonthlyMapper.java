package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.MerchantMonthStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsMonthlyMapper {
    List<MerchantMonthStat> getMerchantMonthStats(Map<String, Object> map);

    void insertMerchantMonthStat(MerchantMonthStat merchantMonthStat);
}
