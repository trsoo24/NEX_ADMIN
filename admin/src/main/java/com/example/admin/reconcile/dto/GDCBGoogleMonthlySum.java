package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GDCBGoogleMonthlySum {
    private String revsCategory;
    private Double productPriceSum;
    private Double taxSum;
    private Double totalAmountSum;
    private Double revShareSum;
}
