package com.example.admin.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInfo {
    private String policyCode; // 결제 정책 이름
    private String flag; // 결제 허용 여부 ( Y / N )
    private String reservationYn; // 변경 예약 여부
    private LocalDate policyRsDt; // 변경 예정 날짜
    private LocalDate createDt; // 최초 생성일
    private LocalDate updateDt; // 최종 변경일
}
