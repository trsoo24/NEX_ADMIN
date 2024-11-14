package com.example.admin.seller.controller;

import com.example.admin.common.response.StatusResult;
import com.example.admin.seller.dto.BlockSellerDto;
import com.example.admin.seller.dto.AdmSeller;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.service.SellerStatDailyService;
import com.example.admin.seller.service.AdmSellerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


    @GetMapping
    public List<AdmSeller> searchAllSeller(@RequestParam("sellerName") String sellerName,
                                                   @RequestParam("startDate") @Valid String startDate,
                                                   @RequestParam("endDate") @Valid String endDate) {
        return admSellerService.searchSeller(sellerName, startDate, endDate);
    }

    @GetMapping("/excel")
    public void exportSellerListExcel(@RequestParam("sellerName") String sellerName,
                                        @RequestParam("startDate") @Valid String startDate,
                                        @RequestParam("endDate") @Valid String endDate,
                                        HttpServletResponse response) throws IllegalAccessException, IOException {
        admSellerService.exportSellerListExcel(sellerName, startDate, endDate, response);
    }

    @PutMapping
    public StatusResult blockSeller(@RequestBody @Valid BlockSellerDto dto) {
        admSellerService.blockSeller(dto);

        return new StatusResult(true);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/day/raw")
    public List<SellerDayStat> getSellerStatsDailyList() {
        return sellerStatDailyService.getSellerStatsDaily();
    }
}
