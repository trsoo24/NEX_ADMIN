package com.example.admin.product.mapper;

import com.example.admin.product.dto.ProductStatsMonthly;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStatsMonthlyMapper {
    List<ProductStatsMonthly> getProductMonthStats(Map<String, Object> map);
}
