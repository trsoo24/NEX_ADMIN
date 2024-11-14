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
    private String regId; // 등록 유저 ID
}
