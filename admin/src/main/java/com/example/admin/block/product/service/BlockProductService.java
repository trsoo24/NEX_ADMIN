package com.example.admin.block.product.service;

import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProduct;
import com.example.admin.block.product.mapper.BlockProductMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockProductService {
    private final BlockProductMapper blockProductMapper;
    private final FunctionUtil functionUtil;

    public void insertBlockProduct(InsertBlockProductDto dto) {
        blockProductMapper.insertBlockProduct(dto);
    }

    public Page<BlockProduct> getBlockProductList(String dcb, String product, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("dcb", dcb);
        map.put("product", product);
        List<BlockProduct> blockProductList = blockProductMapper.getBlockProductList(map);

        return functionUtil.toPage(blockProductList, page, pageSize);
    }

    public void deleteBlockProduct(DeleteBlockProductDto dto) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < dto.getProducts().size(); i++) {
            map.put("dcb", dto.getDcb().toUpperCase());
            map.put("productNo", dto.getProducts().get(i));

            blockProductMapper.deleteBlockProduct(map);
        }
    }
}
