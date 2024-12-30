package com.example.admin.reconcile.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    CODE1("Outstanding", "Outstanding"),
    CODE2("invoice Details", "Invoice Details"),
    CODE3("invoice Summary", "Invoice Summary"),
    CODE4("Invoice Details_기존 결제", "Invoice Details_기존결제"),
    CODE5("Invoice Details_소액 결제", "Invoice Details_소액결제"),
    CODE6("구글 기준 월 대사 (전체)", "구글기준 월대사_전체"),
    CODE7("구글 기준 월 대사 (불일치)", "구글기준 월대사_불일치"),
    CODE8("소액 결제 일 대사", "소액결제 일대사"),
;
    private final String codeName;
    private final String codeRealName;
}

