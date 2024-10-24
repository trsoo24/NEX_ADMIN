package com.example.admin.voc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProvisioningInfo {
    private Integer subNo;
    private String ctn;
    private Integer version;
    private String correlationId;
    private String operatorUserToken;
    private String userLocale;
    private String billingAgreement;
    private String result;
    private String isProvisioned;
    private String tosUrl;
    private String tosVersion;
    private String accountType;
    private String getProvisioningTransactionId;
    private Integer transactionLimit;
    private String paymentType;
    private String updateDt;
    private String createDt;
}
