package com.example.admin.repository.mapper.merchant;

import com.example.admin.domain.entity.merchant.MerchantDayStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsDailyMapper {
    List<MerchantDayStat> getMerchantDayStats(Map<String, Object> map);
    void insertMerchantDayStat(MerchantDayStat merchantDayStat);
}
