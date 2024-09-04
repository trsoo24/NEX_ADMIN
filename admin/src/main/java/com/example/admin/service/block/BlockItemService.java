package com.example.admin.service.block;

import com.example.admin.domain.dto.block.DeleteBlockItemDto;
import com.example.admin.domain.dto.block.InsertBlockItemDto;
import com.example.admin.domain.entity.block.BlockItem;
import com.example.admin.repository.mapper.block.BlockItemMapper;
import com.example.admin.service.FunctionUtil;
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

        for (int i = 0; i < dto.getItems().size(); i++) {
            map.put("dcb", dto.getDcb().toUpperCase());
            map.put("itemNo", dto.getItems().get(i));

            blockItemMapper.deleteBlockItem(map);
        }
    }
}
