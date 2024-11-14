package com.example.admin.sdcb.billing_history.controller;

import com.example.admin.sdcb.billing_history.dto.ErrorBillingHistory;
import com.example.admin.sdcb.billing_history.dto.BillingHistory;
import com.example.admin.sdcb.billing_history.service.SdcbBillingHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


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
    public List<ErrorBillingHistory> getSdcbErrorBillingHistory(@RequestParam("startDate") @Valid String startDate,
                                                                @RequestParam("endDate") @Valid String endDate,
                                                                @RequestParam("case") @Valid String caseCode,
                                                                @RequestParam("ctn") @Valid String ctn) {
        return sdcbBillingHistoryService.getBillingHistoryList(startDate, endDate, caseCode, ctn);
    }

    @GetMapping("/error/sdcb/excel")
    public void getSdcbErrorBillingHistoryExcel(@RequestParam("startDate") @Valid String startDate,
                                                @RequestParam("endDate") @Valid String endDate,
                                                @RequestParam("case") @Valid String caseCode,
                                                @RequestParam("ctn") @Valid String ctn,
                                                HttpServletResponse response) throws IOException {
        sdcbBillingHistoryService.exportErrorBillingHistoryExcel(startDate, endDate, caseCode, ctn, response);
    }
}
