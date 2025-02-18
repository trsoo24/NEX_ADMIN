package com.example.admin.payment_attempt_history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttemptHistory {
    private String ctn;
    private String createDt;
    private String uniqueKey;
    private String productName;
    private Double amount;
    private String resultCode;
}