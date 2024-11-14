package com.example.admin.block.fee_type.service;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.mapper.BlockFeeTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockFeeTypeService {
    private final BlockFeeTypeMapper blockFeeTypeMapper;

    public void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto) {
        if (!existFeeType(blockFeeTypeDto.getFeeTypeCode())) {
            blockFeeTypeMapper.insertBlockFeeType(blockFeeTypeDto);
        }
    }

    public List<BlockFeeTypeDto> getAllBlockFeeType() {
        return blockFeeTypeMapper.getAllBlockFeeType();
    }

    public void deleteBlockFeeType(DeleteFeeTypeDto dto) {
        List<String> feeTypeCode = dto.getFeeTypeCodes();

        blockFeeTypeMapper.deleteBlockFeeType(feeTypeCode);
    }

    private boolean existFeeType(String feeTypeCode) {
        return blockFeeTypeMapper.existsFeeType(feeTypeCode);
    }
}
