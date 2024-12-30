package com.example.admin.block.fee_type.mapper;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockFeeTypeMapper {
    void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto);

    boolean existsFeeType(String feeTypeCode);

    List<BlockFeeTypeDto> getAllBlockFeeType();

    boolean deleteBlockFeeType(String feeTypeCd);
}
