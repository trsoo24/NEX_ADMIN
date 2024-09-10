package com.example.admin.domain.dto.range.field;

import lombok.Getter;

@Getter
public enum RangeMonthField {
    stat_month("기간"),
    a_stat("결제 구간"),
    b_stat("결제자 수"),
    c_stat("전체 결제 건수"),
    d_stat("결제 금액 (원)"),
    e_stat("환불/취소 금액 (원)"),
    f_stat("실 결제 금액 (원)"),
    g_stat("인당 결제 금액(원)")
    ;

    private final String description;

    RangeMonthField(String description) {
        this.description = description;
    }
}
