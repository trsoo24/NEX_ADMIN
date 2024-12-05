package com.example.admin.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInfo {
    private String policyName; // 결제 정책 이름
    private String flag; // 결제 허용 여부 ( Y / N )
}
