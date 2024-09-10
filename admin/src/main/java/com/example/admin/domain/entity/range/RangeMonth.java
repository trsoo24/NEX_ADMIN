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
    private String stat_month; // 기간
    private String a_stat; // 결제 구간 A , 1 ~ 5 , O ( Over )
    private Double b_stat; // 결제자 수
    private Double c_stat; // 전체 결제건 수
    private Double d_stat; // 결제 금액 ( 원 )
    private Double e_stat; // 취소 금액 ( 원 )
    private Double f_stat; // 실 결제 금액 ( 원 )
    private Double g_stat; // 인당 결제 금액 ( 원 )

    public void calculateFStat() {
        this.f_stat = this.d_stat - this.e_stat;
    }

    public void addValue(double value) {
        this.b_stat++;
        this.c_stat++;
        if (value >= 0) {
            this.d_stat += value;
        } else {
            this.e_stat += value;
        }
        this.f_stat += value;
    }

    public void calculateGStat() {
        this.g_stat = this.d_stat / this.b_stat;
    }

    public void roundDFG() {
        this.d_stat = (double) Math.round(this.d_stat);
        this.f_stat = (double) Math.round(this.f_stat);
        this.g_stat = (double) Math.round(this.g_stat);
    }

    public static RangeMonth setDefault(String statMonth, String aStat) {
        double def = 0;
        return RangeMonth.builder()
                .stat_month(statMonth)
                .a_stat(aStat)
                .b_stat(def)
                .c_stat(def)
                .d_stat(def)
                .e_stat(def)
                .f_stat(def)
                .g_stat(def)
                .build();
    }
}
