package com.example.admin.refund.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillingType {
    CANCEL("CANCEL"),
    CHARGE("CHARGE"),
    REFUND("REFUND")
    ;


    private final String type;
}
