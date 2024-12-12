package com.example.admin.product.mapper;

import com.example.admin.product.dto.GetProductStatDto;
import com.example.admin.product.dto.ProductStatsDaily;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStatsDailyMapper {
    List<ProductStatsDaily> getProductDayStats(GetProductStatDto dto);
}
