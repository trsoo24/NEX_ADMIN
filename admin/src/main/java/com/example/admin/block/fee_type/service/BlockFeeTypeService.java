package com.example.admin.block.fee_type.service;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.mapper.BlockFeeTypeMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockFeeTypeService {
    private final BlockFeeTypeMapper blockFeeTypeMapper;
    private final FunctionUtil functionUtil;

    public void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto) {
        if (!existFeeType(blockFeeTypeDto.getFeeTypeCd())) {
            blockFeeTypeMapper.insertBlockFeeType(blockFeeTypeDto);
        }
    }

    public Page<BlockFeeTypeDto> getAllBlockFeeType(String dcb, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("dcb", dcb);

        List<BlockFeeTypeDto> blockFeeTypeDtoList = blockFeeTypeMapper.getAllBlockFeeType(map);

        return functionUtil.toPage(blockFeeTypeDtoList, page, pageSize);
    }

    public void deleteBlockFeeType(DeleteFeeTypeDto dto) {
        List<String> feeTypeCode = dto.getFeeTypeCds();
        Map<String, String> map = new HashMap<>();
        map.put("dcb", dto.getDcb());
        for (String feeType : feeTypeCode) {
            if (blockFeeTypeMapper.existsFeeType(feeType)) {
                map.put("feeTypeCd", feeType);
                blockFeeTypeMapper.deleteBlockFeeType(map);
            }
        }
    }

    private boolean existFeeType(String feeTypeCode) {
        return blockFeeTypeMapper.existsFeeType(feeTypeCode);
    }
}
