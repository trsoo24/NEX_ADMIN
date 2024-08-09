package com.example.admin.controller;

import com.example.admin.domain.dto.payment.PaymentExcelDto;
import com.example.admin.service.payment.DayPaymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/day")
public class DayPaymentController {
    private final DayPaymentService dayPaymentService;

    @PostMapping("/test/add")
    public void add(@RequestParam("date") String date) {
        dayPaymentService.insertDayPayment(date);
    }

    @GetMapping()
    public ResponseEntity<Map<String, List<Object>>> getDayPayments(@RequestParam("month") String month, @RequestParam("dcb") String dcb) {
        return ResponseEntity.ok(dayPaymentService.getDayPaymentDtoForm(month, dcb));
    }

    @GetMapping("/excel")
    public void exportExcel(@RequestParam("month") String month, @RequestParam("dcb") String dcb, HttpServletResponse response) throws IOException {
        dayPaymentService.exportDayPaymentExcel(month, dcb, response);
    }

    @PostMapping("/excel")
    public void postExportExcel(@RequestBody @Valid PaymentExcelDto dto, HttpServletResponse response) throws IOException{
        dayPaymentService.exportDayPaymentExcel2(dto, response);
    }
}
