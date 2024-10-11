package com.example.admin.domain.entity.gdcb.type;

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
