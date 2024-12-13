package com.example.admin.block.fee_type.controller;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.service.BlockFeeTypeService;
import com.example.admin.common.response.StatusResult;
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
    public StatusResult insertBlockFeeType(@RequestBody @Valid InsertBlockFeeTypeDto dto) {
        blockFeeTypeService.insertBlockFeeType(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public List<BlockFeeTypeDto> getBlockFeeTypes() {
        return blockFeeTypeService.getAllBlockFeeType();
    }

    @DeleteMapping
    public StatusResult deleteBlockFeeType(@RequestBody @Valid DeleteFeeTypeDto dto) {
        blockFeeTypeService.deleteBlockFeeType(dto);

        return new StatusResult(true);
    }

    @GetMapping("/check")
    public StatusResult checkFeeType(@RequestParam("feeTypeCode") String feeTypeCode) {
        boolean result = blockFeeTypeService.existFeeType(feeTypeCode);

        return new StatusResult(result);
    }
}
