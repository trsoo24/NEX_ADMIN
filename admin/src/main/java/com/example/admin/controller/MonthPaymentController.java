package com.example.admin.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.entity.payment.MonthPayment;
import com.example.admin.service.payment.MonthPaymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/month")
public class MonthPaymentController {
    private final MonthPaymentService monthPaymentService;

    @PostMapping("/test")
    public StatusResult insertMonthPayment(@RequestParam("year") @Valid String year) {
        monthPaymentService.insertMonthPayment(year);

        return new StatusResult(true);
    }

    @GetMapping()
    public MapResult<String, List<Object>> getMonthPayment(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("year") @Valid String year) {
        Map<String, List<Object>> monthPaymentMap = monthPaymentService.getMonthPaymentDtoForm(year, dcbs);

        return new MapResult<>(true, monthPaymentMap);
    }

    @GetMapping("/2")
    public ListResult<MonthPayment> getDayPayments2(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month) {
        List<MonthPayment> monthPaymentList = monthPaymentService.getMonthPayment(dcbs, month);

        return new ListResult<>(true, monthPaymentList);
    }

    @GetMapping("/excel")
    public void exportExcel(@RequestParam("dcbs") List<String> dcbs, @RequestParam("year") String year, HttpServletResponse response) throws IOException,IllegalAccessException {
        monthPaymentService.exportMonthPaymentExcel(year, dcbs, response);
    }
}
