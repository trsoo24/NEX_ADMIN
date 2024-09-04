package com.example.admin.controller;

import com.example.admin.domain.dto.block.BlockCtnDto;
import com.example.admin.domain.dto.block.DeleteBlockCtnDto;
import com.example.admin.domain.dto.block.InsertBlockCtnDto;
import com.example.admin.service.block.BlockCtnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/ctn")
public class BlockCtnController {
    private final BlockCtnService blockCtnService;

    @PostMapping()
    public void insertBlockFeeType(@RequestBody @Valid InsertBlockCtnDto dto) {
        blockCtnService.insertBlockCtn(dto);
    }

    @GetMapping()
    public Page<BlockCtnDto> getBlockDtoList(@RequestParam("dcb") @Valid String dcb,
                                             @RequestParam("ctn") @Valid String ctn,
                                             @RequestParam("page") @Valid Integer page,
                                             @RequestParam("pageSize") @Valid Integer pageSize) {
        return blockCtnService.getAllBlockCtn(dcb, ctn, page, pageSize);
    }

    @DeleteMapping()
    public void deleteBlockCtn(@RequestBody @Valid DeleteBlockCtnDto dto) {
        blockCtnService.deleteBlockFeeType(dto);
    }
}
