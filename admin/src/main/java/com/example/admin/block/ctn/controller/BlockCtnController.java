package com.example.admin.block.ctn.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.block.ctn.dto.BlockCtnDto;
import com.example.admin.block.ctn.dto.DeleteBlockCtnDto;
import com.example.admin.block.ctn.dto.InsertBlockCtnDto;
import com.example.admin.block.ctn.service.BlockCtnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public PageResult<BlockCtnDto> getBlockDtoList(@RequestParam("dcb") @Valid String dcb,
                                                   @RequestParam("page") @Valid Integer page,
                                                   @RequestParam("pageSize") @Valid Integer pageSize) {
        Page<BlockCtnDto> dtoPage = blockCtnService.getAllBlockCtn(dcb, page, pageSize);

        return new PageResult<>(true, dtoPage);
    }

    @DeleteMapping()
    public StatusResult deleteBlockCtn(@RequestBody @Valid DeleteBlockCtnDto dto) {
        blockCtnService.deleteBlockFeeType(dto);

        return new StatusResult(true);
    }
}
