package com.example.admin.range_day.dto.field;

import lombok.Getter;

@Getter
public enum AStatRange {
    A("A", "전체"),
    B("1", "0 ~ 10 만원"),
    C("2", "10 ~ 20 만원"),
    D("3", "20 ~ 30 만원"),
    E("4", "30 ~ 40 만원"),
    F("5", "40 ~ 50 만원"),
    G("O", "50 만원 이상"),
    ;

    private final String a_stat;
    private final String range;

    AStatRange(String a_stat, String range) {
        this.a_stat = a_stat;
        this.range = range;
    }
}
