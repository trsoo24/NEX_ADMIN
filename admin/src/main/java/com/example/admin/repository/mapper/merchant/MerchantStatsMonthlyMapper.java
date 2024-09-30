package com.example.admin.repository.mapper.merchant;

import com.example.admin.domain.entity.merchant.MerchantMonthStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsMonthlyMapper {
    List<MerchantMonthStat> getMerchantMonthStats(Map<String, Object> map);

    void insertMerchantMonthStat(MerchantMonthStat merchantMonthStat);
}
