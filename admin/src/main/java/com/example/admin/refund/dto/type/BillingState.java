package com.example.admin.refund.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillingState {
    AUTH("A"),
    CHARGE("B"),
    CANCEL("C"),
    REFUND("R")
    ;

    private final String billingState;
}
