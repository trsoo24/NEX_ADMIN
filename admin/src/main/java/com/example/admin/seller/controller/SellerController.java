package com.example.admin.seller.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.seller.dto.InsertSellerDayStat;
import com.example.admin.seller.dto.InsertSellerMonthStat;
import com.example.admin.seller.dto.SellerDayStatDto;
import com.example.admin.seller.dto.SellerMonthStatDto;
import com.example.admin.seller.dto.BlockSellerDto;
import com.example.admin.seller.dto.InsertSellerInfo;
import com.example.admin.seller.dto.AdmSeller;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.dto.SellerMonthStat;
import com.example.admin.seller.service.SellerStatDailyService;
import com.example.admin.seller.service.SellerStatsMonthlyService;
import com.example.admin.seller.service.AdmSellerService;
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
@RequestMapping("/sellers")
public class SellerController {
    private final AdmSellerService admSellerService;
    private final SellerStatDailyService sellerStatDailyService;
    private final SellerStatsMonthlyService sellerStatsMonthlyService;


    @PostMapping
    public StatusResult insertSeller(@RequestBody @Valid InsertSellerInfo dto) {
        admSellerService.insertSeller(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public PageResult<AdmSeller> searchAllSeller(@RequestParam("dcb") @Valid String dcb,
                                                   @RequestParam("sellerName") String sellerName,
                                                   @RequestParam("startDate") @Valid String startDate,
                                                   @RequestParam("endDate") @Valid String endDate,
                                                   @RequestParam("page") @Valid int page,
                                                   @RequestParam("pageSize") @Valid int pageSize) {
        Page<AdmSeller> admSellerPage = admSellerService.searchSellerToPage(dcb, sellerName, startDate, endDate, page, pageSize);

        return new PageResult<>(true, admSellerPage);
    }

    @GetMapping("/excel")
    public void exportSellerListExcel(@RequestParam("dcb") @Valid String dcb,
                                        @RequestParam("sellerName") String sellerName,
                                        @RequestParam("startDate") @Valid String startDate,
                                        @RequestParam("endDate") @Valid String endDate,
                                        HttpServletResponse response) throws IllegalAccessException, IOException {
        admSellerService.exportSellerListExcel(dcb, sellerName, startDate, endDate, response);
    }

    @PutMapping
    public StatusResult blockSeller(@RequestBody @Valid BlockSellerDto dto) {
        admSellerService.blockSeller(dto);

        return new StatusResult(true);
    }

    @GetMapping("/day")
    public PageResult<SellerDayStatDto> getProductStatDailyPage(@RequestParam("dcb") @Valid String dcb,
                                                             @RequestParam("month") @Valid String month,
                                                             @RequestParam("sellerName") @Valid String sellerName,
                                                             @RequestParam("page") @Valid int page,
                                                             @RequestParam("pageSize") @Valid int pageSize) {
        Page<SellerDayStatDto> productDayStatDtoPage = sellerStatDailyService.getSellerStatDailyPage(dcb, month, sellerName, page, pageSize);

        return new PageResult<>(true, productDayStatDtoPage);
    }

    @GetMapping("/day/excel")
    public void getProductStatDailyExcel(@RequestParam("dcb") @Valid String dcb,
                                                           @RequestParam("month") @Valid String month,
                                                           @RequestParam("sellerName") @Valid String sellerName,
                                                           HttpServletResponse response) throws IOException {
        sellerStatDailyService.exportSellerStatDailyExcel(dcb, month, sellerName, response);
    }

    @GetMapping("/month")
    public PageResult<SellerMonthStatDto> getProductStatMonthlyPage(@RequestParam("dcb") @Valid String dcb,
                                                                 @RequestParam("year") @Valid String year,
                                                                 @RequestParam("sellerName") @Valid String sellerName,
                                                                 @RequestParam("page") @Valid int page,
                                                                 @RequestParam("pageSize") @Valid int pageSize) {
        Page<SellerMonthStatDto> productMonthStatDtoPage = sellerStatsMonthlyService.getSellerStatsMonthlyPage(dcb, year, sellerName, page, pageSize);

        return new PageResult<>(true, productMonthStatDtoPage);
    }


    @GetMapping("/month/excel")
    public void getProductStatMonthlyExcel(@RequestParam("dcb") @Valid String dcb,
                                                               @RequestParam("year") @Valid String year,
                                                               @RequestParam("sellerName") @Valid String sellerName,
                                                                HttpServletResponse response) throws IOException {
        sellerStatsMonthlyService.exportSellerStatMonthlyExcel(dcb, year, sellerName, response);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/day/2")
    public List<SellerDayStat> getSellerStatsDailyList(@RequestParam("dcb") @Valid String dcb,
                                                         @RequestParam("month") @Valid String month,
                                                         @RequestParam("sellerName") @Valid String sellerName) {
        return sellerStatDailyService.getSellerStatsDaily(dcb, month, sellerName);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/month/2")
    public List<SellerMonthStat> getSellerStatMonthly(@RequestParam("dcb") @Valid String dcb,
                                                        @RequestParam("year") @Valid String year,
                                                        @RequestParam("sellerName") @Valid String sellerName) {
        return sellerStatsMonthlyService.getSellerStatsMonthly(dcb, year, sellerName);
    }

    @PostMapping("/day/add")
    public void insertDayTest(@RequestBody @Valid InsertSellerDayStat dayStat) {
        sellerStatDailyService.insertTestValue(dayStat);
    }

    @PostMapping("/month/add")
    public void insertMonthTest(@RequestBody @Valid InsertSellerMonthStat dayStat) {
        sellerStatsMonthlyService.insertMonthlyStat(dayStat);
    }
}
