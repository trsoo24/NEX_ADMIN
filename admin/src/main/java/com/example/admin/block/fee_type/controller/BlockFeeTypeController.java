package com.example.admin.block.fee_type.controller;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.service.BlockFeeTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/fee-types")
public class BlockFeeTypeController {
    private final BlockFeeTypeService blockFeeTypeService;

    @PostMapping
    public void insertBlockFeeType(@RequestBody @Valid InsertBlockFeeTypeDto dto) {
        blockFeeTypeService.insertBlockFeeType(dto);
    }

    @GetMapping
    public List<BlockFeeTypeDto> getBlockFeeTypes() {
        return blockFeeTypeService.getAllBlockFeeType();
    }

    @DeleteMapping
    public void deleteBlockFeeType(@RequestBody @Valid DeleteFeeTypeDto dto) {
        blockFeeTypeService.deleteBlockFeeType(dto);
    }
}
