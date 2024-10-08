package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.item.*;
import com.example.admin.service.item.ItemStatsDailyService;
import com.example.admin.service.item.ItemStatsMonthlyService;
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
    private final ItemStatsDailyService itemStatsDailyService;
    private final ItemStatsMonthlyService itemStatsMonthlyService;


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
                                        HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
        productInfoService.exportExcel(dcb, productName, startDate, endDate, response);
    }

    @GetMapping("/day")
    public PageResult<ItemStatsDailyDto> getItemStatsDailyPage(@RequestParam("dcb") @Valid String dcb,
                                                               @RequestParam("month") @Valid String month,
//                                                               @RequestParam("merchantName") @Valid String merchantName,
                                                               @RequestParam("itemName") @Valid String itemName,
                                                               @RequestParam("page") @Valid int page,
                                                               @RequestParam("pageSize") @Valid int pageSize) {
        Page<ItemStatsDailyDto> itemStatsDailyDtoPage = itemStatsDailyService.getItemStatsDailyPage(dcb, month, itemName, page, pageSize);

        return new PageResult<>(true, itemStatsDailyDtoPage);
    }

    @GetMapping("/day/excel")
    public void getItemStatsDailyExcel(@RequestParam("dcb") @Valid String dcb,
                                       @RequestParam("month") @Valid String month,
//                                       @RequestParam("merchantName") @Valid String merchantName,
                                       @RequestParam("itemName") @Valid String itemName,
                                       HttpServletResponse response) throws IOException {
        itemStatsDailyService.exportItemStatDailyExcel(dcb, month, itemName, response);
    }

    @GetMapping("/month")
    public PageResult<ItemStatsMonthlyDto> getItemStatsMonthlyPage(@RequestParam("dcb") @Valid String dcb,
                                                                   @RequestParam("year") @Valid String year,
//                                                                   @RequestParam("merchantName") @Valid String merchantName,
                                                                   @RequestParam("itemName") @Valid String itemName,
                                                                   @RequestParam("page") @Valid int page,
                                                                   @RequestParam("pageSize") @Valid int pageSize) {
        Page<ItemStatsMonthlyDto> itemStatsMonthlyDtoPage = itemStatsMonthlyService.getItemStatsMonthlyPage(dcb, year, itemName, page, pageSize);

        return new PageResult<>(true, itemStatsMonthlyDtoPage);
    }

    @GetMapping("/month/excel")
    public void getItemStatsMonthlyExcel(@RequestParam("dcb") @Valid String dcb,
                                         @RequestParam("year") @Valid String year,
//                                         @RequestParam("merchantName") @Valid String merchantName,
                                         @RequestParam("itemName") @Valid String itemName,
                                         HttpServletResponse response) throws IOException {
        itemStatsMonthlyService.exportItemStatsMonthlyExcel(dcb, year, itemName, response);
    }

    @PostMapping("/month/add")
    public void addItemStatMonthTest(@RequestBody @Valid InsertItemMonthStat itemMonthStat) {
        itemStatsMonthlyService.insertStatMonthly(itemMonthStat);
    }

    @PostMapping("/day/add")
    public void addItemStatDayTest(@RequestBody @Valid InsertItemDayStat itemDayStat) {
        itemStatsDailyService.insertStatDaily(itemDayStat);
    }
}
