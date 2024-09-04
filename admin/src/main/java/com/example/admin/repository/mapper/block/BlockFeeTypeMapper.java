package com.example.admin.repository.mapper.block;

import com.example.admin.domain.dto.block.BlockFeeTypeDto;
import com.example.admin.domain.dto.block.InsertBlockFeeTypeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface BlockFeeTypeMapper {
    void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto);

    boolean existsFeeType(String feeTypeCode);

    List<BlockFeeTypeDto> getAllBlockFeeType(Map<String, Object> map);

    int countBlockFeeType();

    void  deleteBlockFeeType(Map<String, String> map);
}
