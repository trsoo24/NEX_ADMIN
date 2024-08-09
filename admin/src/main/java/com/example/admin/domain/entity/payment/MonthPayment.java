package com.example.admin.domain.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthPayment {
    private String statMonth; // '2024-07' 방식 저장
    private Double aStat; // 총 금액
    private Double bStat; // 구매 금액
    private String cStat; // 구매 금액 비율
    private Double dStat; // 취소 금액
    private String eStat; // 취소 금액 비율
    private Double fStat; // 환불 금액
    private String gStat; // 환불 금액 비율
    private Double hStat; // 총 결제 건 수
    private Double iStat; // 구매 수
    private String jStat; // 구매 건 수 비율
    private Double kStat; // 취소 수
    private String lStat; // 취소 건 수 비율
    private Double mStat; // 환불 수
    private String nStat; // 환불 건 수 비율
    private Double pStat;    // 건별 평균 구매액
    private Double rStat; // 건별 평균 취소액
    private Double tStat; // 건별 평균 환불액

    public static MonthPayment toTotal() {
        double def = 0;
        return MonthPayment.builder()
                .statMonth("TOTAL")
                .aStat(def)
                .bStat(def)
                .dStat(def)
                .fStat(def)
                .hStat(def)
                .iStat(def)
                .kStat(def)
                .mStat(def)
                .pStat(def)
                .rStat(def)
                .tStat(def)
                .build();
    }

    public void addTotalAmount(MonthPayment MonthPayment) {
        this.aStat += MonthPayment.getAStat();
        this.bStat += MonthPayment.getBStat();
        this.dStat += MonthPayment.getDStat();
        this.fStat += MonthPayment.getFStat();
        this.hStat += MonthPayment.getHStat();
        this.iStat += MonthPayment.getIStat();
        this.kStat += MonthPayment.getKStat();
        this.mStat += MonthPayment.getMStat();
    }
}
