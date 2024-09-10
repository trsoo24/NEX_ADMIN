package com.example.admin.controller;

import com.example.admin.common.response.DataResult;
import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.entity.payment.DayPayment;
import com.example.admin.service.payment.DayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/payment/day")
public class DayPaymentController {
    private final DayPaymentService dayPaymentService;

    @PostMapping("/test/add")
    public StatusResult add(@RequestParam("date") String date) {
        dayPaymentService.insertDayPayment(date);

        return new StatusResult(true);
    }

    @GetMapping
    public MapResult<String, List<Object>> getDayPayments(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month) {
        Map<String, List<Object>> dayPaymentMap = dayPaymentService.getDayPaymentDtoForm(dcb, month);

        return new MapResult<>(true, dayPaymentMap);
    }

    @GetMapping("/2")
    public DayPayment getDayPayments2(@RequestParam("dcb") @Valid String dcb, @RequestParam("day") @Valid String day) {
        return dayPaymentService.getDayPayment(dcb, day);
    }

    @GetMapping("/excel")
    public StatusResult exportExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month, HttpServletResponse response) throws IOException {
        dayPaymentService.exportDayPaymentExcel(month, dcb, response);

        return new StatusResult(true);
    }

    @GetMapping("/excel/2")
    public StatusResult exportExcel2(HttpServletRequest request, @RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month, HttpServletResponse response) throws IOException, IllegalAccessException {
        dayPaymentService.exportDayPaymentExcel2(request, month, dcb, response);

        return new StatusResult(true);
    }
}
