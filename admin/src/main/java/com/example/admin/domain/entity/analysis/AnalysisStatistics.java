package com.example.admin.domain.entity.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisStatistics {
    private String createDt;
    private String totalCount; // 총 결제 시도 건수
    private Integer successCount; // 결제 성공 수
    private Integer failureCount; // 결제 실패 수
    private Integer code51100070; // LGT 고객 정보 없음
    private Integer code52000002; // 6등급 연체자 결제 불가
    private Integer code52000003; // 7등급 연체자 결제 불가
    private Integer code52101200; // 직권 차단
    private Integer code51000012; // 약관 미동의 고객
    private Integer code30000009; // 재동의 필요 고객
    private Integer code30000008; // 법정 대리인 동의 필요 (청소년)
    private Integer code41000001; // BLOCK CTN
    private Integer code41000002; // BLOCK FEE_TYPE
    private Integer code52104008; // 월 잔여 한도 초과 (RBP)
    private Integer code53104008; // 월 잔여 한도 초과 (RCSG)
    private Integer code30000002; // 제공된 토큰 만료
    private Integer codeOther; // 기타 사유
    private String dcb;
}
