package com.example.admin.payment_attempt_history.controller;

import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistoryDto;
import com.example.admin.payment_attempt_history.service.PaymentAttemptHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/voc/history")
@RequiredArgsConstructor
public class PaymentAttemptHistoryController {
    private final PaymentAttemptHistoryService paymentAttemptHistoryService;


    // 통합 ADMIN 에서 페이징하기 위해 list 값으로 종합
    @GetMapping("/payment-attempt")
    public List<PaymentAttemptHistoryDto> getPaymentAttemptHistoryList(@RequestParam("startDate") @Valid String startDate,
                                                                       @RequestParam("endDate") @Valid String endDate,
                                                                       @RequestParam("ctn") @Valid String ctn) {
        return paymentAttemptHistoryService.getPaymentAttemptHistoryDtoList(startDate, endDate, ctn);
    }
}
