package com.example.admin.sdcb.billing_history.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.sdcb.billing_history.dto.ErrorBillingHistory;
import com.example.admin.sdcb.billing_history.dto.BillingHistory;
import com.example.admin.sdcb.billing_history.service.SdcbBillingHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/reconciliation")
@RequiredArgsConstructor
public class BillingHistoryController {
    private final SdcbBillingHistoryService sdcbBillingHistoryService;


    @PostMapping("/sdcb")
    public void insertBillingHistory(@RequestBody @Valid BillingHistory billingHistory) {
        sdcbBillingHistoryService.insertBillingHistory(billingHistory);
    }

    @GetMapping("/error/sdcb")
    public PageResult<ErrorBillingHistory> getSdcbErrorBillingHistory(@RequestParam("dcb") @Valid String dcb,
                                                                      @RequestParam("startDate") @Valid String startDate,
                                                                      @RequestParam("endDate") @Valid String endDate,
                                                                      @RequestParam("case") @Valid String caseCode,
                                                                      @RequestParam("ctn") @Valid String ctn,
                                                                      @RequestParam("page") @Valid int page,
                                                                      @RequestParam("pageSize") @Valid int pageSize) {
        Page<ErrorBillingHistory> errorBillingHistoryPage = sdcbBillingHistoryService.errorBillingHistoryToPage(dcb, startDate, endDate, caseCode, ctn, page, pageSize);

        return new PageResult<>(true, errorBillingHistoryPage);
    }

    @GetMapping("/error/sdcb/excel")
    public void getSdcbErrorBillingHistoryExcel(@RequestParam("dcb") @Valid String dcb,
                                                @RequestParam("startDate") @Valid String startDate,
                                                @RequestParam("endDate") @Valid String endDate,
                                                @RequestParam("case") @Valid String caseCode,
                                                @RequestParam("ctn") @Valid String ctn,
                                                HttpServletResponse response) throws IOException {
        sdcbBillingHistoryService.exportErrorBillingHistoryExcel(dcb, startDate, endDate, caseCode, ctn, response);
    }
}
