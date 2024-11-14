package com.example.admin.block.product.service;

import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProductDto;
import com.example.admin.block.product.mapper.BlockProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockProductService {
    private final BlockProductMapper blockProductMapper;

    public void insertBlockProduct(InsertBlockProductDto dto) {
        blockProductMapper.insertBlockProduct(dto);
    }

    public List<BlockProductDto> getBlockProductList(String product) {
        Map<String, Object> map = new HashMap<>();
        map.put("product", product);

        return blockProductMapper.getBlockProductList(map);
    }

    public void deleteBlockProduct(DeleteBlockProductDto dto) {
        blockProductMapper.deleteBlockProduct(dto.getProducts());
    }
}
