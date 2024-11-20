package com.example.admin.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInfo {
    private String policyCode; // 결제 정책 이름
    private String flag; // 결제 허용 여부 ( Y / N )
    private String reservationYn; // 변경 예약 여부
    private Date policyRsDt; // 변경 예정 날짜
    private Date createDt; // 최초 생성일
    private Date updateDt; // 최종 변경일
}
