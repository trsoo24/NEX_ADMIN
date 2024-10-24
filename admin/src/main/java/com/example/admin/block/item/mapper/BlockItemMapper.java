package com.example.admin.block.item.mapper;

import com.example.admin.block.item.dto.InsertBlockItemDto;
import com.example.admin.block.item.dto.BlockItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockItemMapper {
    void insertBlockItem(InsertBlockItemDto insertBlockItemDto);
    List<BlockItem> getBlockItemList(Map<String, Object> map);
    void deleteBlockItem(Map<String, Object> map);
}
