package com.example.admin.repository.mapper.block;

import com.example.admin.domain.dto.block.InsertBlockItemDto;
import com.example.admin.domain.entity.block.BlockItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockItemMapper {
    void insertBlockItem(InsertBlockItemDto insertBlockItemDto);
    List<BlockItem> getBlockItemList(Map<String, Object> map);
    void deleteBlockItem(Map<String, Object> map);
}
