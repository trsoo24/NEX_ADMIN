package com.example.admin.block.product.controller;

import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProductDto;
import com.example.admin.block.product.service.BlockProductService;
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
    public void insertBlockProduct(@RequestBody @Valid InsertBlockProductDto dto) {
        blockProductService.insertBlockProduct(dto);
    }

    @GetMapping
    public List<BlockProductDto> getBlockProductList(@RequestParam("product") @Valid String product) {
        return blockProductService.getBlockProductList(product);
    }

    @DeleteMapping
    public void deleteBlockProduct(@RequestBody @Valid DeleteBlockProductDto dto) {
        blockProductService.deleteBlockProduct(dto);
    }
}
