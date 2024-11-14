package com.example.admin.block.ctn.service;

import com.example.admin.block.ctn.dto.BlockCtnDto;
import com.example.admin.block.ctn.dto.DeleteBlockCtnDto;
import com.example.admin.block.ctn.dto.InsertBlockCtnDto;
import com.example.admin.block.ctn.mapper.BlockCtnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockCtnService {
    private final BlockCtnMapper blockCtnMapper;

    public void insertBlockCtn(InsertBlockCtnDto blockCtnDto) {
        if (!existBlockCtn(blockCtnDto.getCtn())) {
            blockCtnMapper.insertBlockCtn(blockCtnDto);
        }
    }

    public List<BlockCtnDto> getAllBlockCtn() {
        return blockCtnMapper.getAllBlockCtn();
    }

    public void deleteBlockCtn(DeleteBlockCtnDto dto) {
        List<String> ctns = dto.getCtns();

        blockCtnMapper.deleteBlockCtn(ctns);
    }

    private boolean existBlockCtn(String ctn) {
        return blockCtnMapper.existsCtn(ctn);
    }
}
