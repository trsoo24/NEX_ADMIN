package com.example.admin.payment_history.controller;

import com.example.admin.payment_history.dto.PaymentHistoryDto;
import com.example.admin.payment_history.service.PaymentHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment-history")
@RequiredArgsConstructor
public class PaymentHistoryController {
    private final PaymentHistoryService paymentHistoryService;

    @GetMapping
    public List<PaymentHistoryDto> getPaymentHistoryDtoList(@RequestParam("startDate") @Valid String startDate,
                                                                  @RequestParam("endDate") @Valid String endDate,
                                                                  @RequestParam("ctn") @Valid String ctn) {
       return paymentHistoryService.getPaymentHistoryDtoList(startDate, endDate, ctn);
    }
}
