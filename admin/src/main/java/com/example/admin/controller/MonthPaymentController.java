package com.example.admin.controller;

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
    public void insertMonthPayment(@RequestParam("year") @Valid String year) {
        monthPaymentService.insertMonthPayment(year);
    }

    @GetMapping()
    public Map<String, List<Object>> getMonthPayment(@RequestParam("dcb") @Valid String dcb, @RequestParam("year") @Valid String year) {
        return monthPaymentService.getMonthPaymentDtoForm(year, dcb);
    }

    @GetMapping("/excel")
    public void exportExcel(@RequestParam("dcb") String dcb, @RequestParam("year") String year, HttpServletResponse response) throws IOException,IllegalAccessException {
        monthPaymentService.exportMonthPaymentExcel(year, dcb, response);
    }
}
