package com.example.admin.domain.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePurchase {
    private String requestId;
    private String transactionId;
    private String partitionId;
    private String purchaseId;
    private String ctn;
    private int subNo;
    private String email;
    private String status;
    private int totAmt;
    private String purchaseDt;
    private String cancelDt;
    private Date notiCreateDt;
    private Date notiUpdateDt;
    private Date sendMmsDt;
    private Date sendEmailDt;
    private String paymentCode;
    private Long productId;
}
