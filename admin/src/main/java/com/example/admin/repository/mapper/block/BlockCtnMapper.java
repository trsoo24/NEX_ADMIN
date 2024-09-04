package com.example.admin.repository.mapper.block;

import com.example.admin.domain.dto.block.BlockCtnDto;
import com.example.admin.domain.dto.block.InsertBlockCtnDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockCtnMapper {
    void insertBlockCtn(InsertBlockCtnDto blockCtnDto);

    boolean existsCtn(String ctn);

    List<BlockCtnDto> getAllBlockCtn(Map<String, Object> map);

    int countBlockCtn();

    void deleteBlockCtn(Map<String, String> map);
}
