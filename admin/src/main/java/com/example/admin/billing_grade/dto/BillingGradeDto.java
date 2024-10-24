package com.example.admin.billing_grade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingGradeDto { // 월별 청구 현황 ( 등급별 ) 조회
    private String custGrdCd; // 고객 등급
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

    public void dtoSetCustGrdCd(String custGrdCd) {
        this.custGrdCd = custGrdCd;
    }

    public static BillingGradeDto toTotalBillingGrade(BillingGradeDto billingGrade, String dcb) {
        return BillingGradeDto.builder()
                .custGrdCd(billingGrade.getCustGrdCd())
                .allAceCnt(billingGrade.getAllAceCnt())
                .allCnt(billingGrade.getAllCnt())
                .allAmount(billingGrade.getAllAmount())
                .paidAceCnt(billingGrade.getPaidAceCnt())
                .paidCnt(billingGrade.getPaidCnt())
                .paidAmount(billingGrade.getPaidAmount())
                .unpaidAceCnt(billingGrade.getUnpaidAceCnt())
                .unpaidCnt(billingGrade.getUnpaidCnt())
                .unpaidAmount(billingGrade.getUnpaidAmount())
                .dcb(dcb).build();
    }

    public void addTotalValue(BillingGradeDto billingGrade) {
        this.allAceCnt += billingGrade.getAllAceCnt();
        this.allCnt += billingGrade.getAllCnt();
        this.allAmount += billingGrade.getAllAmount();
        this.paidAceCnt += billingGrade.getPaidAceCnt();
        this.paidCnt += billingGrade.getPaidCnt();
        this.paidAmount += billingGrade.getPaidAmount();
        this.unpaidAceCnt += billingGrade.getUnpaidAceCnt();
        this.unpaidCnt += billingGrade.getUnpaidCnt();
        this.unpaidAmount += billingGrade.getUnpaidAmount();
    }
}
