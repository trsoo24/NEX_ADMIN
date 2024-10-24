package com.example.admin.voc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsInfoDto {
    private String googleUserToken;
    private String operatorUserToken;
    private Integer expireTime;
    private String updateDt;
    private String createDt;
}
