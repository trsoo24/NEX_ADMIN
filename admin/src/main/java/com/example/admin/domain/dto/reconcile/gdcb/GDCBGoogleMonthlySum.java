package com.example.admin.domain.dto.reconcile.gdcb;

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
    private Double itemPriceSum;
    private Double taxSum;
    private Double totalAmountSum;
    private Double revShareSum;
}
