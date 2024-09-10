package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.item.InsertProductInfoDto;
import com.example.admin.domain.dto.item.ProductInfo;
import com.example.admin.domain.entity.item.Product;
import com.example.admin.service.item.ProductInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductInfoService productInfoService;


    @PostMapping()
    public StatusResult insertProductInfo(@RequestBody @Valid InsertProductInfoDto dto) {
        productInfoService.createProductInfo(dto);

        return new StatusResult(true);
    }

    @GetMapping()
    public PageResult<ProductInfo> getAllProductInfo(@RequestParam("dcb") @Valid String dcb, @RequestParam("productName") @Valid String productName,
                                                     @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate,
                                                     @RequestParam("page") @Valid int page, @RequestParam("pageSize") @Valid int pageSize) {
        Page<ProductInfo> productInfoPage =  productInfoService.getAllProductInfo(dcb, productName, startDate, endDate, page, pageSize);

        return new PageResult<>(true, productInfoPage);
    }

    @GetMapping("/excel")
    public StatusResult getProductExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("productName") @Valid String productName,
                                        @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
        productInfoService.exportExcel(dcb, productName, startDate, endDate, response);

        return new StatusResult(true);
    }
}
