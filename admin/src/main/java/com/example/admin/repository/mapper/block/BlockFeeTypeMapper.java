package com.example.admin.repository.mapper.block;

import com.example.admin.domain.dto.block.BlockFeeTypeDto;
import com.example.admin.domain.dto.block.InsertBlockFeeTypeDto;
import com.example.admin.domain.entity.block.BlockFeeType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockFeeTypeMapper {
    void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto);

    boolean existsFeeType(String feeTypeCode);

    List<BlockFeeTypeDto> getAllBlockFeeType(Map<String, Integer> map);

    int countBlockFeeType();

    void  deleteBlockFeeType(String feeTypeCode);
}
