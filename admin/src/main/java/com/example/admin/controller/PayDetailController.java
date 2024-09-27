package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.payment.PayDetailDto;
import com.example.admin.service.payment.PayDetailService;
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
@RequestMapping("/payment")
public class PayDetailController {
    private final PayDetailService payDetailService;

    @GetMapping("/sdcb")
    public PageResult<PayDetailDto> getPayDetailPage(@RequestParam("dcb") @Valid String dcb,
                                                     @RequestParam("selectedPaymentTypes") @Valid List<String> selectedPaymentTypes,
                                                     @RequestParam("startDate") @Valid String startDate,
                                                     @RequestParam("endDate") @Valid String endDate,
                                                     @RequestParam("searchType") @Valid String searchType,
                                                     @RequestParam("keyword") @Valid String keyword,
                                                     @RequestParam("page") @Valid int page,
                                                     @RequestParam("pageSize") @Valid int pageSize) {
        Page<PayDetailDto> payDetailDtoPage = payDetailService.getPayDetailPage(dcb, selectedPaymentTypes, startDate, endDate, searchType, keyword, page, pageSize);

        return new PageResult<>(true, payDetailDtoPage);
    }

    @GetMapping("/excel/sdcb")
    public void exportSdcbPayDetailExcel(@RequestParam("dcb") @Valid String dcb,
                                                 @RequestParam("selectedPaymentTypes") @Valid List<String> selectedPaymentTypes,
                                                 @RequestParam("startDate") @Valid String startDate,
                                                 @RequestParam("endDate") @Valid String endDate,
                                                 @RequestParam("searchType") @Valid String searchType,
                                                 @RequestParam("keyword") @Valid String keyword,
                                                 HttpServletResponse response) throws IOException, IllegalAccessException {
        payDetailService.exportExcel(dcb, selectedPaymentTypes, startDate, endDate, searchType, keyword, response);
    }
}
