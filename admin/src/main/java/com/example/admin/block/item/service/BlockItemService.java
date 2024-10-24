package com.example.admin.block.item.service;

import com.example.admin.block.item.dto.DeleteBlockItemDto;
import com.example.admin.block.item.dto.InsertBlockItemDto;
import com.example.admin.block.item.dto.BlockItem;
import com.example.admin.block.item.mapper.BlockItemMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockItemService {
    private final BlockItemMapper blockItemMapper;
    private final FunctionUtil functionUtil;

    public void insertBlockItem(InsertBlockItemDto dto) {
        blockItemMapper.insertBlockItem(dto);
    }

    public Page<BlockItem> getBlockItemList(String dcb, String item, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("dcb", dcb);
        map.put("item", item);
        List<BlockItem> blockItemList = blockItemMapper.getBlockItemList(map);

        return functionUtil.toPage(blockItemList, page, pageSize);
    }

    public void deleteBlockItem(DeleteBlockItemDto dto) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < dto.getProducts().size(); i++) {
            map.put("dcb", dto.getDcb().toUpperCase());
            map.put("itemNo", dto.getProducts().get(i));

            blockItemMapper.deleteBlockItem(map);
        }
    }
}
