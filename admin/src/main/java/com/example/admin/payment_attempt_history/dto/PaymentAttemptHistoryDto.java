package com.example.admin.payment_attempt_history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAttemptHistoryDto {
    private String ctn;
    private String createDt;
    private String dcb;
    private String itemName;
    private String amount;
    private String resultCode;

    public static PaymentAttemptHistoryDto toDto(PaymentAttemptHistory paymentAttemptHistory) {
        return PaymentAttemptHistoryDto.builder()
                .ctn(paymentAttemptHistory.getCtn())
                .createDt(paymentAttemptHistory.getCreateDt())
                .dcb("GDCB")
                .itemName(paymentAttemptHistory.getItemName())
                .amount(paymentAttemptHistory.getAmount())
                .resultCode(paymentAttemptHistory.getResultCode())
                .build();
    }
}
