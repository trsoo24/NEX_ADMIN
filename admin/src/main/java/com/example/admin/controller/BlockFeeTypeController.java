package com.example.admin.controller;

import com.example.admin.domain.dto.block.BlockFeeTypeDto;
import com.example.admin.domain.dto.block.InsertBlockFeeTypeDto;
import com.example.admin.domain.entity.block.BlockFeeType;
import com.example.admin.service.block.BlockFeeTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/fee")
public class BlockFeeTypeController {
    private final BlockFeeTypeService blockFeeTypeService;

    @PostMapping()
    public void insertBlockFeeType(@RequestBody @Valid InsertBlockFeeTypeDto dto) {
        blockFeeTypeService.insertBlockFeeType(dto);
    }

    @GetMapping()
    public Page<BlockFeeTypeDto> getBlockFeeTypes(@RequestParam("page") @Valid Integer page, @RequestParam("pageSize") @Valid Integer pageSize) {
        return blockFeeTypeService.getAllBlockFeeType(page, pageSize);
    }

    @DeleteMapping()
    public void deleteBlockFeeType(@RequestParam("feeTypeCd") List<String> feeTypeCd) {
        blockFeeTypeService.deleteBlockFeeType(feeTypeCd);
    }
}
