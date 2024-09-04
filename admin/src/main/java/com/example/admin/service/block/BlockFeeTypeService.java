package com.example.admin.service.block;

import com.example.admin.domain.dto.block.BlockFeeTypeDto;
import com.example.admin.domain.dto.block.DeleteFeeTypeDto;
import com.example.admin.domain.dto.block.InsertBlockFeeTypeDto;
import com.example.admin.repository.mapper.block.BlockFeeTypeMapper;
import com.example.admin.service.FunctionUtil;
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

    public Page<BlockFeeTypeDto> getAllBlockFeeType(String dcb, String feeTypeCd, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("dcb", dcb);
        map.put("feeTypeCd", feeTypeCd);

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
