package com.example.admin.service.block;

import com.example.admin.domain.dto.block.BlockCtnDto;
import com.example.admin.domain.dto.block.DeleteBlockCtnDto;
import com.example.admin.domain.dto.block.InsertBlockCtnDto;
import com.example.admin.repository.mapper.block.BlockCtnMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlockCtnService {
    private final BlockCtnMapper blockCtnMapper;
    private final FunctionUtil functionUtil;

    public void insertBlockCtn(InsertBlockCtnDto blockCtnDto) {
        if (!existBlockCtn(blockCtnDto.getCtn())) {
            blockCtnMapper.insertBlockCtn(blockCtnDto);
        }
    }

    public Page<BlockCtnDto> getAllBlockCtn(String dcb, String ctn, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("dcb", dcb);
        map.put("ctn", ctn);

        List<BlockCtnDto> blockCtnDtoList = blockCtnMapper.getAllBlockCtn(map);
        return functionUtil.toPage(blockCtnDtoList, page, pageSize);
    }

    public void deleteBlockFeeType(DeleteBlockCtnDto dto) {
        List<String> ctnList = dto.getCtns();
        Map<String, String> map = new HashMap<>();

        for (String ctn : ctnList) {
            if (blockCtnMapper.existsCtn(ctn)) {
                map.put("dcb", dto.getDcb());
                map.put("ctn", ctn);

                blockCtnMapper.deleteBlockCtn(map);
            }
        }
    }

    private boolean existBlockCtn(String ctn) {
        return blockCtnMapper.existsCtn(ctn);
    }
}
