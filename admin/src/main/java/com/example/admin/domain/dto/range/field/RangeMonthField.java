package com.example.admin.domain.dto.range.field;

import lombok.Getter;

@Getter
public enum RangeMonthField {
    statMonth("기간"),
    aStat("결제 구간"),
    bStat("결제자 수"),
    cStat("전체 결제 건수"),
    dStat("결제 금액 (원)"),
    eStat("환불/취소 금액 (원)"),
    fStat("실 결제 금액 (원)"),
    gStat("인당 결제 금액(원)")
    ;

    private final String description;

    RangeMonthField(String description) {
        this.description = description;
    }
}
