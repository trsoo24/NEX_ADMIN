package com.example.admin.product.mapper;

import com.example.admin.product.dto.ProductStatsDaily;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStatsDailyMapper {
    void insertProductStatDaily(ProductStatsDaily productStatsDaily);

    List<ProductStatsDaily> getProductDayStats(Map<String ,Object> map);
}
