package com.example.admin.block.ctn.controller;

import com.example.admin.block.ctn.dto.BlockCtnDto;
import com.example.admin.block.ctn.dto.DeleteBlockCtnDto;
import com.example.admin.block.ctn.dto.InsertBlockCtnDto;
import com.example.admin.block.ctn.service.BlockCtnService;
import com.example.admin.common.response.StatusResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/ctns")
public class BlockCtnController {
    private final BlockCtnService blockCtnService;

    @PostMapping
    public StatusResult insertBlockFeeType(@RequestBody @Valid InsertBlockCtnDto dto) {
        blockCtnService.insertBlockCtn(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public List<BlockCtnDto> getBlockDtoList() {
        List<BlockCtnDto> dtoPage = blockCtnService.getAllBlockCtn();

        return dtoPage;
    }

    @DeleteMapping
    public StatusResult deleteBlockCtn(@RequestBody @Valid DeleteBlockCtnDto dto) {
        blockCtnService.deleteBlockCtn(dto);

        return new StatusResult(true);
    }
}
