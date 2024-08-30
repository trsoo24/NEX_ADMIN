package com.example.admin.service.block;

import com.example.admin.domain.dto.block.BlockCtnDto;
import com.example.admin.domain.dto.block.DeleteBlockCtnDto;
import com.example.admin.domain.dto.block.InsertBlockCtnDto;
import com.example.admin.repository.mapper.block.BlockCtnMapper;
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
public class BlockCtnService {
    private final BlockCtnMapper blockCtnMapper;

    public void insertBlockCtn(InsertBlockCtnDto blockCtnDto) {
        if (!existBlockCtn(blockCtnDto.getCtn())) {
            blockCtnMapper.insertBlockCtn(blockCtnDto);
        }
    }

    public Page<BlockCtnDto> getAllBlockCtn(int page, int pageSize, String dcb) {
        Map<String, Integer> map = new HashMap<>();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        List<BlockCtnDto> blockCtnDtoList = blockCtnMapper.getAllBlockCtn(map);
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return new PageImpl<>(blockCtnDtoList, pageable, blockCtnMapper.countBlockCtn());
    }

    public void deleteBlockFeeType(DeleteBlockCtnDto dto) {
        List<String> ctnList = dto.getCtns();

        for (String ctn : ctnList) {
            if (blockCtnMapper.existsCtn(ctn)) {
                blockCtnMapper.deleteBlockCtn(ctn);
            }
        }
    }

    private boolean existBlockCtn(String ctn) {
        return blockCtnMapper.existsCtn(ctn);
    }
}
