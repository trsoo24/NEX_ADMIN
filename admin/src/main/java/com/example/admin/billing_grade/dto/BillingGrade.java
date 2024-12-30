package com.example.admin.billing_grade.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BillingGrade { // 월별 청구 현황 ( 등급별 ) 조회
    private String custGrdCd; // 고객 등급
    private String statYyMm;
    private String regDt;
    private Integer allAceCnt; // 전체 청구 대상 (명)
    private Integer allCnt; // 전체 청구 건수 (건)
    private Integer allAmount; // 전체 청구액 (원)
    private Integer paidAceCnt; // 정상
    private Integer paidCnt;
    private Integer paidAmount;
    private Integer unpaidAceCnt; // 연체
    private Integer unpaidCnt;
    private Integer unpaidAmount;
    private final String dcb = "GDCB";
}
