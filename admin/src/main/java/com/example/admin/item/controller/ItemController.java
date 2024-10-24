package com.example.admin.item.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.item.dto.ItemStatsDaily;
import com.example.admin.item.dto.ItemStatsMonthly;
import com.example.admin.item.dto.*;
import com.example.admin.item.service.ItemStatsDailyService;
import com.example.admin.item.service.ItemStatsMonthlyService;
import com.example.admin.item.service.ProductInfoService;
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
public class ItemController {
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
                                        HttpServletResponse response) throws IOException, IllegalAccessException {
        productInfoService.exportExcel(dcb, productName, startDate, endDate, response);
    }

    @GetMapping("/day/2")
    public List<ItemStatsDaily> getItemStatsDailyList(@RequestParam("dcb") @Valid String dcb,
                                                            @RequestParam("month") @Valid String month,
                                                            @RequestParam("productName") @Valid String productName) {
        return itemStatsDailyService.getItemStatsDailyList(dcb, month, productName);
    }

    @GetMapping("/day")
    public PageResult<ItemStatsDailyDto> getItemStatsDailyPage(@RequestParam("dcb") @Valid String dcb,
                                                               @RequestParam("month") @Valid String month,
                                                               @RequestParam("productName") @Valid String productName,
                                                               @RequestParam("page") @Valid int page,
                                                               @RequestParam("pageSize") @Valid int pageSize) {
        Page<ItemStatsDailyDto> itemStatsDailyDtoPage = itemStatsDailyService.getItemStatsDailyPage(dcb, month, productName, page, pageSize);

        return new PageResult<>(true, itemStatsDailyDtoPage);
    }

    @GetMapping("/day/excel")
    public void getItemStatsDailyExcel(@RequestParam("dcb") @Valid String dcb,
                                       @RequestParam("month") @Valid String month,
                                       @RequestParam("productName") @Valid String productName,
                                       HttpServletResponse response) throws IOException {
        itemStatsDailyService.exportItemStatDailyExcel(dcb, month, productName, response);
    }

    @GetMapping("/month")
    public PageResult<ItemStatsMonthlyDto> getItemStatsMonthlyPage(@RequestParam("dcb") @Valid String dcb,
                                                                   @RequestParam("year") @Valid String year,
                                                                   @RequestParam("productName") @Valid String productName,
                                                                   @RequestParam("page") @Valid int page,
                                                                   @RequestParam("pageSize") @Valid int pageSize) {
        Page<ItemStatsMonthlyDto> itemStatsMonthlyDtoPage = itemStatsMonthlyService.getItemStatsMonthlyPage(dcb, year, productName, page, pageSize);

        return new PageResult<>(true, itemStatsMonthlyDtoPage);
    }

    @GetMapping("/month/2")
    public List<ItemStatsMonthly> getItemStatsMonthlyList(@RequestParam("dcb") @Valid String dcb,
                                                          @RequestParam("year") @Valid String year,
                                                          @RequestParam("productName") @Valid String productName) {
        return itemStatsMonthlyService.getItemStatsMonthlyList(dcb, year, productName);
    }

    @GetMapping("/month/excel")
    public void getItemStatsMonthlyExcel(@RequestParam("dcb") @Valid String dcb,
                                         @RequestParam("year") @Valid String year,
                                         @RequestParam("productName") @Valid String productName,
                                         HttpServletResponse response) throws IOException {
        itemStatsMonthlyService.exportItemStatsMonthlyExcel(dcb, year, productName, response);
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
