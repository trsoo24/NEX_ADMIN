package com.example.admin.refund.dto;

import com.example.admin.auth.dto.AuthInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDto {
    private String correlationId;
    private String ctn;
    private String createDt;
    private String itemName;
    private Long itemPrice;
    private String transactionType;
    private String dcb;

    public static RefundDto toRefundDto(AuthInfo authInfo) {
        return RefundDto.builder()
                .correlationId(authInfo.getCorrelationId())
                .ctn(authInfo.getCtn())
                .createDt(authInfo.getCreateDt())
                .itemName(authInfo.getItemName())
                .itemPrice(authInfo.getItemPrice())
                .transactionType(authInfo.getTransactionType())
                .build();
    }
}
