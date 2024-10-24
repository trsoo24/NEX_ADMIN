package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.MerchantDayStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsDailyMapper {
    List<MerchantDayStat> getMerchantDayStats(Map<String, Object> map);
    void insertMerchantDayStat(MerchantDayStat merchantDayStat);
}
