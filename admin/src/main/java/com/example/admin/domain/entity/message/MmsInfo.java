package com.example.admin.domain.entity.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MmsInfo {
    private String dcb;
    private String regDt;
    private String ctn;
    private String smsType;
    private String content;
    private String result;
}
