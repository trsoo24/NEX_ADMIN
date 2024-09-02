package com.example.admin.domain.dto.billing.field;

import lombok.Getter;

@Getter
public enum BillingGradeField {
    custGrdCd("구분"),
    allAceCnt("청구 대상(명)"),
    allCnt("청구 건수(건)"),
    allAmount("청구액(원)"),
    paidAceCnt("청구 대상(명)"),
    paidCnt("청구 건수(건)"),
    paidAmount("청구액(원)"),
    unpaidAceCnt("청구 대상(명)"),
    unpaidCnt("청구 건수(건)"),
    unpaidAmount("청구액(원)")
    ;

    private final String description;

    BillingGradeField(String description) {this.description = description;}
}
