package com.example.admin.block.product.mapper;

import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockProductMapper {
    void insertBlockProduct(InsertBlockProductDto insertBlockProductDto);
    List<BlockProduct> getBlockProductList(Map<String, Object> map);
    void deleteBlockProduct(Map<String, Object> map);
}
