package com.example.admin.domain.entity.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String dcb;

    public static BillingGrade toBillingGrade(String custGrdCd, String statYyMm, String regDt, int allAceCnt, int allCnt, int allAmount,
                                              int paidAceCnt, int paidCnt, int paidAmount, int unpaidAceCnt, int unpaidCnt, int unpaidAmount, String dcb) {
        return BillingGrade.builder()
                .custGrdCd(custGrdCd)
                .statYyMm(statYyMm)
                .regDt(regDt)
                .allAceCnt(allAceCnt)
                .allCnt(allCnt)
                .allAmount(allAmount)
                .paidAceCnt(paidAceCnt)
                .paidCnt(paidCnt)
                .paidAmount(paidAmount)
                .unpaidAceCnt(unpaidAceCnt)
                .unpaidCnt(unpaidCnt)
                .unpaidAmount(unpaidAmount)
                .dcb(dcb).build();
    }
}
