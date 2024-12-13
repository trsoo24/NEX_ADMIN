package com.example.admin.block.product.controller;

import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProductDto;
import com.example.admin.block.product.service.BlockProductService;
import com.example.admin.common.response.StatusResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/products")
public class BlockProductController {
    private final BlockProductService blockProductService;

    @PostMapping
    public StatusResult insertBlockProduct(@RequestBody @Valid InsertBlockProductDto dto) {
        blockProductService.insertBlockProduct(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public List<BlockProductDto> getBlockProductList(@RequestParam("product") @Valid String product) {
        return blockProductService.getBlockProductList(product);
    }

    @DeleteMapping
    public StatusResult deleteBlockProduct(@RequestBody @Valid DeleteBlockProductDto dto) {
        blockProductService.deleteBlockProduct(dto);

        return new StatusResult(true);
    }

    @GetMapping("/check")
    public StatusResult checkBlockProduct(@RequestParam("product") String product) {
        boolean result = blockProductService.existsProduct(product);

        return new StatusResult(result);
    }
}
