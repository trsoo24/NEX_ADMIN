package com.example.admin.refund.dto;

import com.example.admin.auth.dto.AuthInfo;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManualRefund {
    private String authTransactionId;
    private String correlationId;
    private String ctn;
    private String itemName;
    private String merchantName;
    private String paymentDescription;
    private String merchantContact;
    private Long total;
    private Long itemPrice;
    private Long tax;
    private Long subNo;
    private String aceNo;
    private String ban;
    private String unitMdl;
    private String feeType;
    private String transactionType;
    private String transactionDt;
    private Date refundDt;
    private Date createDt;

    public static ManualRefund initRefundWork(AuthInfo reqAuth) {
        return ManualRefund.builder()
                .aceNo(reqAuth.getAceNo())
                .authTransactionId(reqAuth.getAuthTransactionId())
                .ban(reqAuth.getBan())
                .correlationId(reqAuth.getCorrelationId())
                .ctn(reqAuth.getCtn())
                .subNo(Long.parseLong(reqAuth.getSubNo()))
                .feeType(reqAuth.getFeeType())
                .unitMdl(reqAuth.getUnitMdl())
                .itemName(reqAuth.getItemName())
                .itemPrice(reqAuth.getItemPrice())
                .tax(reqAuth.getTax())
                .total(reqAuth.getTotal())
                .merchantContact(reqAuth.getMerchantContact())
                .merchantName(reqAuth.getMerchantName())
                .paymentDescription(reqAuth.getMerchantName() + " - " + reqAuth.getItemName())
                .transactionDt(reqAuth.getTransactionDt())
                .transactionType(reqAuth.getTransactionType())
                .build();
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }
}
