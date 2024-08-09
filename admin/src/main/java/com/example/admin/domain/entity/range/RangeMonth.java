package com.example.admin.domain.entity.range;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangeMonth {
    private String statMonth; // 기간
    private String aStat; // 결제 구간 A , 1 ~ 5 , O ( Over )
    private Double bStat; // 결제자 수
    private Double cStat; // 전체 결제건 수
    private Double dStat; // 결제 금액 ( 원 )
    private Double eStat; // 취소 금액 ( 원 )
    private Double fStat; // 실 결제 금액 ( 원 )
    private Double gStat; // 인당 결제 금액 ( 원 )

    public void calculateFStat() {
        this.fStat = this.dStat - this.eStat;
    }

    public void addValue(double value) {
        this.bStat++;
        this.cStat++;
        if (value >= 0) {
            this.dStat += value;
        } else {
            this.eStat += value;
        }
        this.fStat += value;
    }

    public void calculateGStat() {
        this.gStat = this.dStat / this.bStat;
    }

    public void roundDFG() {
        this.dStat = (double) Math.round(this.dStat);
        this.fStat = (double) Math.round(this.fStat);
        this.gStat = (double) Math.round(this.gStat);
    }

    public static RangeMonth setDefault(String statMonth, String aStat) {
        double def = 0;
        return RangeMonth.builder()
                .statMonth(statMonth)
                .aStat(aStat)
                .bStat(def)
                .cStat(def)
                .dStat(def)
                .eStat(def)
                .fStat(def)
                .gStat(def)
                .build();
    }
}
