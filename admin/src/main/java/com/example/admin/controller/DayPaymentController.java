package com.example.admin.controller;

import com.example.admin.common.response.DataResult;
import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.payment.DayPaymentDto;
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
    public MapResult<String, Map<String, List<Object>>> getDayPayments(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month) {
        // DayPaymentDto List 컬럼이름1 : ~~~, 컬럼이름2 : ~~~ , ...

        Map<String, Map<String, List<Object>>> dayPaymentMap = dayPaymentService.getDayPaymentDtoForm(dcbs, month);

        return new MapResult<>(true, dayPaymentMap);
    }

    @GetMapping("/2")
    public MapResult<String, List<DayPayment>> getDayPayments2(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month) {
        // DayPayment List 날짜별 값 ~

        Map<String, List<DayPayment>> dayPaymentMap = dayPaymentService.getDayPayment(dcbs, month);

        return new MapResult<>(true, dayPaymentMap);
    }

    @GetMapping("/excel")
    public void exportExcel(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month, HttpServletResponse response) throws IOException {
        dayPaymentService.exportDayPaymentExcel(month, dcbs, response);
    }

    @GetMapping("/excel/2")
    public void exportExcel2(HttpServletRequest request, @RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month, HttpServletResponse response) throws IOException, IllegalAccessException {
        dayPaymentService.exportDayPaymentExcel2(request, month, dcbs, response);
    }
}
