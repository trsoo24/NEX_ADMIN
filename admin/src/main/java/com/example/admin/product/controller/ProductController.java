package com.example.admin.product.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.dto.ProductStatsMonthly;
import com.example.admin.product.dto.*;
import com.example.admin.product.service.ProductStatsDailyService;
import com.example.admin.product.service.ProductStatsMonthlyService;
import com.example.admin.product.service.ProductInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ProductStatsMonthlyService productStatsMonthlyService;


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
    public void getProductExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("productName") @Valid String productName,
                                        @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate,
                                        HttpServletResponse response) throws IOException, IllegalAccessException {
        productInfoService.exportExcel(dcb, productName, startDate, endDate, response);
    }

    @GetMapping("/day/2")
    public List<ProductStatsDaily> getProductStatsDailyList(@RequestParam("dcb") @Valid String dcb,
                                                         @RequestParam("month") @Valid String month,
                                                         @RequestParam("productName") @Valid String productName) {
        return productStatsDailyService.getProductStatsDailyList(dcb, month, productName);
    }

    @GetMapping("/day")
    public PageResult<ProductStatsDailyDto> getProductStatsDailyPage(@RequestParam("dcb") @Valid String dcb,
                                                                  @RequestParam("month") @Valid String month,
                                                                  @RequestParam("productName") @Valid String productName,
                                                                  @RequestParam("page") @Valid int page,
                                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<ProductStatsDailyDto> productStatsDailyDtoPage = productStatsDailyService.getProductStatsDailyPage(dcb, month, productName, page, pageSize);

        return new PageResult<>(true, productStatsDailyDtoPage);
    }

    @GetMapping("/day/excel")
    public void getProductStatsDailyExcel(@RequestParam("dcb") @Valid String dcb,
                                       @RequestParam("month") @Valid String month,
                                       @RequestParam("productName") @Valid String productName,
                                       HttpServletResponse response) throws IOException {
        productStatsDailyService.exportProductStatDailyExcel(dcb, month, productName, response);
    }

    @GetMapping("/month")
    public PageResult<ProductStatsMonthlyDto> getProductStatsMonthlyPage(@RequestParam("dcb") @Valid String dcb,
                                                                   @RequestParam("year") @Valid String year,
                                                                   @RequestParam("productName") @Valid String productName,
                                                                   @RequestParam("page") @Valid int page,
                                                                   @RequestParam("pageSize") @Valid int pageSize) {
        Page<ProductStatsMonthlyDto> productStatsMonthlyDtoPage = productStatsMonthlyService.getProductStatsMonthlyPage(dcb, year, productName, page, pageSize);

        return new PageResult<>(true, productStatsMonthlyDtoPage);
    }

    @GetMapping("/month/2")
    public List<ProductStatsMonthly> getProductStatsMonthlyList(@RequestParam("dcb") @Valid String dcb,
                                                             @RequestParam("year") @Valid String year,
                                                             @RequestParam("productName") @Valid String productName) {
        return productStatsMonthlyService.getProductStatsMonthlyList(dcb, year, productName);
    }

    @GetMapping("/month/excel")
    public void getProductStatsMonthlyExcel(@RequestParam("dcb") @Valid String dcb,
                                         @RequestParam("year") @Valid String year,
                                         @RequestParam("productName") @Valid String productName,
                                         HttpServletResponse response) throws IOException {
        productStatsMonthlyService.exportProductStatsMonthlyExcel(dcb, year, productName, response);
    }

    @PostMapping("/month/add")
    public void addProductStatMonthTest(@RequestBody @Valid InsertProductMonthStat productMonthStat) {
        productStatsMonthlyService.insertStatMonthly(productMonthStat);
    }

    @PostMapping("/day/add")
    public void addProductStatDayTest(@RequestBody @Valid InsertProductDayStat productDayStat) {
        productStatsDailyService.insertStatDaily(productDayStat);
    }
}
