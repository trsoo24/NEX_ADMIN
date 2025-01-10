package com.example.admin.payment_attempt_history.dto;

import com.example.admin.common.service.FunctionUtil;
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
    private String uniqueKey;
    private String productName;
    private Double amount;
    private String resultCode;

    public static PaymentAttemptHistoryDto toDto(PaymentAttemptHistory paymentAttemptHistory) {
        return PaymentAttemptHistoryDto.builder()
                .ctn(FunctionUtil.transCtn(paymentAttemptHistory.getCtn()))
                .createDt(paymentAttemptHistory.getCreateDt())
                .uniqueKey(paymentAttemptHistory.getUniqueKey())
                .dcb("GDCB")
                .productName(paymentAttemptHistory.getProductName())
                .amount(paymentAttemptHistory.getAmount())
                .resultCode(paymentAttemptHistory.getResultCode())
                .build();
    }
}
