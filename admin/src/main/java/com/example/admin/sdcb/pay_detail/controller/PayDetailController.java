package com.example.admin.sdcb.pay_detail.controller;

import com.example.admin.sdcb.pay_detail.dto.PayDetailDto;
import com.example.admin.sdcb.pay_detail.service.PayDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PayDetailController {
    private final PayDetailService payDetailService;

    @GetMapping("/sdcb")
    public List<PayDetailDto> getPayDetailList(@RequestParam("selectedPaymentTypes") @Valid List<String> selectedPaymentTypes,
                                               @RequestParam("startDate") @Valid String startDate,
                                               @RequestParam("endDate") @Valid String endDate,
                                               @RequestParam("searchType") @Valid String searchType,
                                               @RequestParam("keyword") @Valid String keyword) {
        return payDetailService.getPayDetailList(selectedPaymentTypes, startDate, endDate, searchType, keyword);
    }

    @GetMapping("/excel/sdcb")
    public void exportSdcbPayDetailExcel(@RequestParam("selectedPaymentTypes") @Valid List<String> selectedPaymentTypes,
                                         @RequestParam("startDate") @Valid String startDate,
                                         @RequestParam("endDate") @Valid String endDate,
                                         @RequestParam("searchType") @Valid String searchType,
                                         @RequestParam("keyword") @Valid String keyword,
                                         HttpServletResponse response) throws IOException, IllegalAccessException {
        payDetailService.exportExcel(selectedPaymentTypes, startDate, endDate, searchType, keyword, response);
    }
}
