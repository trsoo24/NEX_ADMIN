package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
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
    public MapResult<String, List<Object>> getMonthPayment(@RequestParam("dcb") @Valid String dcb, @RequestParam("year") @Valid String year) {
        Map<String, List<Object>> monthPaymentMap = monthPaymentService.getMonthPaymentDtoForm(year, dcb);

        return new MapResult<>(true, monthPaymentMap);
    }

    @GetMapping("/excel")
    public StatusResult exportExcel(@RequestParam("dcb") String dcb, @RequestParam("year") String year, HttpServletResponse response) throws IOException,IllegalAccessException {
        monthPaymentService.exportMonthPaymentExcel(year, dcb, response);

        return new StatusResult(true);
    }
}
