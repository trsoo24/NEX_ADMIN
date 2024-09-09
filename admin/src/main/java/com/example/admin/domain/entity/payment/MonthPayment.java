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
    private String stat_month; // '2024-07' 방식 저장
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

    public static MonthPayment toTotal() {
        double def = 0;
        return MonthPayment.builder()
                .stat_month("TOTAL")
                .a_stat(def)
                .b_stat(def)
                .d_stat(def)
                .f_stat(def)
                .h_stat(def)
                .i_stat(def)
                .k_stat(def)
                .m_stat(def)
                .p_stat(def)
                .r_stat(def)
                .t_stat(def)
                .build();
    }

    public void addTotalAmount(MonthPayment MonthPayment) {
        this.a_stat += MonthPayment.getA_stat();
        this.b_stat += MonthPayment.getB_stat();
        this.d_stat += MonthPayment.getD_stat();
        this.f_stat += MonthPayment.getF_stat();
        this.h_stat += MonthPayment.getH_stat();
        this.i_stat += MonthPayment.getI_stat();
        this.k_stat += MonthPayment.getK_stat();
        this.m_stat += MonthPayment.getM_stat();
    }
}
