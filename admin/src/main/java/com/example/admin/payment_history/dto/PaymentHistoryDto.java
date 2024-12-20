package com.example.admin.payment_history.dto;

import com.example.admin.common.service.FunctionUtil;
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
    private Double total; // itemPrice + tax
    private String itemName;
    private String merchantName;

    public static PaymentHistoryDto toDto(PaymentHistory paymentHistory) {
        return PaymentHistoryDto.builder()
                .ctn(FunctionUtil.transCtn(paymentHistory.getCtn()))
                .transactionDt(paymentHistory.getTransactionDt())
                .cancelNotificationDt(paymentHistory.getCancelNotificationDt())
                .total(paymentHistory.getTotal())
                .itemName(paymentHistory.getItemName())
                .merchantName(paymentHistory.getMerchantName())
                .build();
    }
}
