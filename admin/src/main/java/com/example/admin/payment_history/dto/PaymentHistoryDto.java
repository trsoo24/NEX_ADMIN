package com.example.admin.payment_history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistoryDto {
    private String ctn;
    private String transactionDt;
    private String cancelNotificationDt;
    private Integer total; // itemPrice + tax
    private String itemName;
    private String merchantName;
    private String dcb;

    public static PaymentHistoryDto toDto(PaymentHistory paymentHistory) {
        return PaymentHistoryDto.builder()
                .transactionDt(paymentHistory.getTransactionDt())
                .dcb("GDCB")
                .itemName(paymentHistory.getItemName())
                .ctn(paymentHistory.getCtn())
                .total(paymentHistory.getTotal())
                .merchantName(paymentHistory.getMerchantName())
                .build();
    }
}
