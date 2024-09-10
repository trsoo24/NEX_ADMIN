package com.example.admin.repository.mapper.item;

import com.example.admin.domain.dto.item.InsertProductInfoDto;
import com.example.admin.domain.dto.item.ProductInfo;
import com.example.admin.domain.entity.item.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductInfoMapper {
    void insertProductInfo(InsertProductInfoDto productInfo);

    boolean existsProduct(String productName);

    List<ProductInfo> getAllProductInfo(Map<String, String> map);
}
