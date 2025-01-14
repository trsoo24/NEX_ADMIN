package com.example.admin.testphone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertTestPhoneDto {
    private String dcb;
    private String ctn;
    private String regId; // 등록 유저 ID
}
