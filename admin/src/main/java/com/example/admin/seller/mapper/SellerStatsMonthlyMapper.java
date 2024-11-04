package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.SellerMonthStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerStatsMonthlyMapper {
    List<SellerMonthStat> getSellerMonthStats(Map<String, Object> map);

    void insertSellerMonthStat(SellerMonthStat sellerMonthStat);
}
