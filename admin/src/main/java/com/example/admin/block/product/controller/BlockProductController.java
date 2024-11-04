package com.example.admin.block.product.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProduct;
import com.example.admin.block.product.service.BlockProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public PageResult<BlockProduct> getBlockProductList(@RequestParam("dcb") @Valid String dcb,
                                                  @RequestParam("product") @Valid String product,
                                                  @RequestParam("page") @Valid int page,
                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<BlockProduct> productPage = blockProductService.getBlockProductList(dcb, product, page, pageSize);

        return new PageResult<>(true, productPage);
    }

    @DeleteMapping
    public StatusResult deleteBlockProduct(@RequestBody @Valid DeleteBlockProductDto dto) {
        blockProductService.deleteBlockProduct(dto);

        return new StatusResult(true);
    }
}
