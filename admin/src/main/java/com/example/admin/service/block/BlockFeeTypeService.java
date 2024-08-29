package com.example.admin.service.block;

import com.example.admin.domain.dto.block.BlockFeeTypeDto;
import com.example.admin.domain.dto.block.InsertBlockFeeTypeDto;
import com.example.admin.repository.mapper.block.BlockFeeTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockFeeTypeService {
    private final BlockFeeTypeMapper blockFeeTypeMapper;

    public void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto) {
        if (!existFeeType(blockFeeTypeDto.getFeeTypeCd())) {
            blockFeeTypeMapper.insertBlockFeeType(blockFeeTypeDto);
        }
    }

    public Page<BlockFeeTypeDto> getAllBlockFeeType(int page, int pageSize, String dcb) {
        Map<String, Integer> map = new HashMap<>();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        List<BlockFeeTypeDto> blockFeeTypeDtoList = blockFeeTypeMapper.getAllBlockFeeType(map);
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return new PageImpl<>(blockFeeTypeDtoList, pageable, blockFeeTypeMapper.countBlockFeeType());
    }

    public void deleteBlockFeeType(List<String> feeTypeCode, String dcb) {
        for (String feeType : feeTypeCode) {
            if (blockFeeTypeMapper.existsFeeType(feeType)) {
                blockFeeTypeMapper.deleteBlockFeeType(feeType);
            }
        }
    }

    private boolean existFeeType(String feeTypeCode) {
        return blockFeeTypeMapper.existsFeeType(feeTypeCode);
    }
}
