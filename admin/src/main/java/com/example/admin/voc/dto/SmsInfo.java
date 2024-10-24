package com.example.admin.voc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsInfo {
    private String ctn;
    private String googleUserToken;
    private String result;
    private String paymentType;
    private String operatorUserToken;
    private Integer outExpireTime;
    private String updateDt;
    private String createDt;
    private String tosVersion;
    private String tosDate;
}
