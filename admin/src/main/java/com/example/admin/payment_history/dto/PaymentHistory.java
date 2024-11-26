package com.example.admin.payment_history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory { // TB_AUTH_INFO 에서 가져올 데이터
    private String ctn;
    private String transactionDt;
    private String cancelNotificationDt;
    private Double total; // itemPrice + tax
    private String itemName;
    private String merchantName;
}
