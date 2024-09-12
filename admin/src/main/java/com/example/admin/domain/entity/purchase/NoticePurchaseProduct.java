package com.example.admin.domain.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePurchaseProduct {
    private String requestId;
    private String transactionId;
    private String partitionId;
    private String purchaseId;
    private String productId;
    private String productName;
    private Long productAmt;
    private String autoYn;
    private int autoPeriodUnit;
    private String autoPeriodValue;
    private String sellerCompany;
    private String sellerName;
    private String sellerEmail;
    private String sellerRegNum;
    private String sellerPhone;
    private String sellerAddress;
    private String status;
    private String purchaseDt;
    private String cancelDt;
    private Date notiCreateDt;
    private Date notiUpdateDt;
}
