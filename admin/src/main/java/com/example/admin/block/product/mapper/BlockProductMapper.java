package com.example.admin.block.product.mapper;

import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockProductMapper {
    boolean insertBlockProduct(InsertBlockProductDto insertBlockProductDto);
    List<BlockProductDto> getBlockProductList(Map<String, Object> map);
    boolean deleteBlockProduct(List<Long> productNos);
    boolean existsProduct(String product);
}
