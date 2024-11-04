package com.example.admin.product.mapper;

import com.example.admin.product.dto.InsertProductInfoDto;
import com.example.admin.product.dto.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductInfoMapper {
    void insertProductInfo(InsertProductInfoDto productInfo);

    boolean existsProduct(String productName);

    List<ProductInfo> getAllProductInfo(Map<String, String> map);
}
