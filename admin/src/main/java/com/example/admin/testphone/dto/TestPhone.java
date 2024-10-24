package com.example.admin.testphone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestPhone {
    private String ctn;
    private String regDt; // 등록 시각
    private String mbrId; // 등록 유저 ID
    private String name;
    private String email;

    public static TestPhone toTestPhone(InsertTestPhoneDto dto) {
        return TestPhone.builder()
                .ctn(dto.getCtn())
                .regDt(dateTime())
                .mbrId("TEST ADMIN")
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    private static String dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd a HH:mm:ss", Locale.KOREA);
        return now.format(formatter);
    }
}
