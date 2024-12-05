package com.example.admin.product.controller;

import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.dto.*;
import com.example.admin.product.service.ProductStatsDailyService;
import com.example.admin.product.service.ProductInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductInfoService productInfoService;
    private final ProductStatsDailyService productStatsDailyService;

    @GetMapping()
    public List<ProductInfo> getProductInfoList(@RequestParam("productName") @Valid String productName,
                                                @RequestParam("startDate") @Valid String startDate,
                                                @RequestParam("endDate") @Valid String endDate) {
        return productInfoService.getProductInfoList(productName, startDate, endDate, false);
    }

    @GetMapping("/excel")
    public void getProductExcel(@RequestParam("productName") @Valid String productName,
                                @RequestParam("startDate") @Valid String startDate,
                                @RequestParam("endDate") @Valid String endDate,
                                HttpServletResponse response) throws IOException, IllegalAccessException {
        productInfoService.exportExcel(productName, startDate, endDate, response);
    }

    @GetMapping("/day/raw")
    public List<ProductStatsDaily> getProductStatsDailyList() {
        return productStatsDailyService.getProductStatsDailyList();
    }
}
