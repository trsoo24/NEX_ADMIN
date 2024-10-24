package com.example.admin.sdcb.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePurchaseType {
    private String requestId;
    private String transactionId;
    private String partitionId;
    private String purchaseId;
    private String paymentCode;
    private String paymentName;
    private Long paymentAmt;
    private String status;
    private String purchaseDt;
    private String cancelDt;
    private Date notiCreateDt;
    private Date notiUpdateDt;
}

