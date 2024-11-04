package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.SellerDayStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerStatsDailyMapper {
    List<SellerDayStat> getSellerDayStats(Map<String, Object> map);
    void insertSellerDayStat(SellerDayStat sellerDayStat);
}
