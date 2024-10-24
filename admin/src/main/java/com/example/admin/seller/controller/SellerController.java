package com.example.admin.seller.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.seller.dto.InsertMerchantDayStat;
import com.example.admin.seller.dto.InsertMerchantMonthStat;
import com.example.admin.seller.dto.MerchantDayStatDto;
import com.example.admin.seller.dto.MerchantMonthStatDto;
import com.example.admin.seller.dto.BlockMerchantDto;
import com.example.admin.seller.dto.InsertMerchantInfo;
import com.example.admin.seller.dto.AdmMerchant;
import com.example.admin.seller.dto.MerchantDayStat;
import com.example.admin.seller.dto.MerchantMonthStat;
import com.example.admin.seller.service.MerchantStatDailyService;
import com.example.admin.seller.service.MerchantStatsMonthlyService;
import com.example.admin.seller.service.AdmMerchantService;
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
@RequestMapping("/seller")
public class SellerController {
    private final AdmMerchantService admMerchantService;
    private final MerchantStatDailyService merchantStatDailyService;
    private final MerchantStatsMonthlyService merchantStatsMonthlyService;


    @PostMapping
    public StatusResult insertMerchant(@RequestBody @Valid InsertMerchantInfo dto) {
        admMerchantService.insertMerchant(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public PageResult<AdmMerchant> searchAllMerchant(@RequestParam("dcb") @Valid String dcb,
                                                     @RequestParam("merchantName") String merchantName,
                                                     @RequestParam("startDate") @Valid String startDate,
                                                     @RequestParam("endDate") @Valid String endDate,
                                                     @RequestParam("page") @Valid int page,
                                                     @RequestParam("pageSize") @Valid int pageSize) {
        Page<AdmMerchant> admMerchantPage = admMerchantService.searchMerchantToPage(dcb, merchantName, startDate, endDate, page, pageSize);

        return new PageResult<>(true, admMerchantPage);
    }

    @GetMapping("/excel")
    public void exportMerchantListExcel(@RequestParam("dcb") @Valid String dcb,
                                        @RequestParam("merchantName") String merchantName,
                                        @RequestParam("startDate") @Valid String startDate,
                                        @RequestParam("endDate") @Valid String endDate,
                                        HttpServletResponse response) throws IllegalAccessException, IOException {
        admMerchantService.exportMerchantListExcel(dcb, merchantName, startDate, endDate, response);
    }

    @PutMapping
    public StatusResult blockMerchant(@RequestBody @Valid BlockMerchantDto dto) {
        admMerchantService.blockMerchant(dto);

        return new StatusResult(true);
    }

    @GetMapping("/day")
    public PageResult<MerchantDayStatDto> getItemStatDailyPage(@RequestParam("dcb") @Valid String dcb,
                                                               @RequestParam("month") @Valid String month,
                                                               @RequestParam("merchantName") @Valid String merchantName,
                                                               @RequestParam("page") @Valid int page,
                                                               @RequestParam("pageSize") @Valid int pageSize) {
        Page<MerchantDayStatDto> itemDayStatDtoPage = merchantStatDailyService.getMerchantStatDailyPage(dcb, month, merchantName, page, pageSize);

        return new PageResult<>(true, itemDayStatDtoPage);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/day/2")
    public List<MerchantDayStat> getMerchantStatsDailyList(@RequestParam("dcb") @Valid String dcb,
                                                           @RequestParam("month") @Valid String month,
                                                           @RequestParam("merchantName") @Valid String merchantName) {
        return merchantStatDailyService.getMerchantStatsDaily(dcb, month, merchantName);
    }

    @GetMapping("/day/excel")
    public void getItemStatDailyExcel(@RequestParam("dcb") @Valid String dcb,
                                                           @RequestParam("month") @Valid String month,
                                                           @RequestParam("merchantName") @Valid String merchantName,
                                                           HttpServletResponse response) throws IOException {
        merchantStatDailyService.exportMerchantStatDailyExcel(dcb, month, merchantName, response);
    }

    @GetMapping("/month")
    public PageResult<MerchantMonthStatDto> getItemStatMonthlyPage(@RequestParam("dcb") @Valid String dcb,
                                                                   @RequestParam("year") @Valid String year,
                                                                   @RequestParam("merchantName") @Valid String merchantName,
                                                                   @RequestParam("page") @Valid int page,
                                                                   @RequestParam("pageSize") @Valid int pageSize) {
        Page<MerchantMonthStatDto> itemMonthStatDtoPage = merchantStatsMonthlyService.getMerchantStatsMonthlyPage(dcb, year, merchantName, page, pageSize);

        return new PageResult<>(true, itemMonthStatDtoPage);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/month/2")
    public List<MerchantMonthStat> getMerchantStatMonthly(@RequestParam("dcb") @Valid String dcb,
                                                          @RequestParam("year") @Valid String year,
                                                          @RequestParam("merchantName") @Valid String merchantName) {
        return merchantStatsMonthlyService.getMerchantStatsMonthly(dcb, year, merchantName);
    }


    @GetMapping("/month/excel")
    public void getItemStatMonthlyExcel(@RequestParam("dcb") @Valid String dcb,
                                                               @RequestParam("year") @Valid String year,
                                                               @RequestParam("merchantName") @Valid String merchantName,
                                                                HttpServletResponse response) throws IOException {
        merchantStatsMonthlyService.exportMerchantStatMonthlyExcel(dcb, year, merchantName, response);
    }

    @PostMapping("/day/add")
    public void insertDayTest(@RequestBody @Valid InsertMerchantDayStat dayStat) {
        merchantStatDailyService.insertTestValue(dayStat);
    }

    @PostMapping("/month/add")
    public void insertMonthTest(@RequestBody @Valid InsertMerchantMonthStat dayStat) {
        merchantStatsMonthlyService.insertMonthlyStat(dayStat);
    }
}
