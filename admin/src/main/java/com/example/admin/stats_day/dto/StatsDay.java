package com.example.admin.stats_day.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDay {
    private String stat_day; // '2024-07-04' 방식 저장
    private Double a_stat; // 총 금액
    private Double b_stat; // 구매 금액
    private String c_stat; // 구매 금액 비율
    private Double d_stat; // 취소 금액
    private String e_stat; // 취소 금액 비율
    private Double f_stat; // 환불 금액
    private String g_stat; // 환불 금액 비율
    private Double h_stat; // 총 결제 건 수
    private Double i_stat; // 구매 수
    private String j_stat; // 구매 건 수 비율
    private Double k_stat; // 취소 수
    private String l_stat; // 취소 건 수 비율
    private Double m_stat; // 환불 수
    private String n_stat; // 환불 건 수 비율
    private Double p_stat;    // 건별 평균 구매액
    private Double r_stat; // 건별 평균 취소액
    private Double t_stat; // 건별 평균 환불액
    private final String dcb = "GDCB";

    public void calculateStat() {
        this.a_stat = calculateTotal(this.b_stat, this.d_stat, this.f_stat);
        this.h_stat = this.i_stat + this.k_stat + this.m_stat;
        this.c_stat = calculatePercent(this.b_stat, this.a_stat);
        this.e_stat = calculatePercent(this.d_stat, this.a_stat);
        this.g_stat = calculatePercent(this.f_stat, this.a_stat);
        this.j_stat = calculatePercent(this.i_stat, this.h_stat);
        this.l_stat = calculatePercent(this.k_stat, this.h_stat);
        this.n_stat = calculatePercent(this.m_stat, this.h_stat);
    }

    private String calculatePercent(double numerator, double denominator) {
        return Math.round(numerator / denominator * 100 * 10) / 10.0 + "%";
    }


    private double calculateTotal(double b_stat, double d_stat, double f_stat) {
        return this.a_stat = b_stat - d_stat - f_stat;
    }
}
