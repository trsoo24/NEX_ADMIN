package com.example.admin.block.fee_type.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.service.BlockFeeTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/fee")
public class BlockFeeTypeController {
    private final BlockFeeTypeService blockFeeTypeService;

    @PostMapping
    public StatusResult insertBlockFeeType(@RequestBody @Valid InsertBlockFeeTypeDto dto) {
        blockFeeTypeService.insertBlockFeeType(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public PageResult<BlockFeeTypeDto> getBlockFeeTypes(@RequestParam("dcb") @Valid String dcb,
                                                        @RequestParam("feeTypeCd") @Valid String feeTypeCd,
                                                        @RequestParam("page") @Valid Integer page,
                                                        @RequestParam("pageSize") @Valid Integer pageSize) {
        Page<BlockFeeTypeDto> dtoPage = blockFeeTypeService.getAllBlockFeeType(dcb, feeTypeCd, page, pageSize);

        return new PageResult<>(true, dtoPage);
    }

    @DeleteMapping
    public StatusResult deleteBlockFeeType(@RequestBody @Valid DeleteFeeTypeDto dto) {
        blockFeeTypeService.deleteBlockFeeType(dto);

        return new StatusResult(true);
    }
}
