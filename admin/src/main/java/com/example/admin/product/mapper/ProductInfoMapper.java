package com.example.admin.product.mapper;

import com.example.admin.product.dto.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductInfoMapper {
    List<ProductInfo> getAllProductInfo(Map<String, String> map);
}
