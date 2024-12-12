package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.GetSellerStatDto;
import com.example.admin.seller.dto.SellerDayStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellerStatsDailyMapper {
    List<SellerDayStat> getSellerDayStats(GetSellerStatDto dto);
}
