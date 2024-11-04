package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleMonthlyInvoiceSum { // Google Monthly invoice fees and expense summary LGU DCB YYYYMM.csv 파일 내용 저장
    private String year;
    private String month;
    private String revsCategory;
    private Double productPriceSum;
    private Double taxSum;
    private Double totalAmountSum;
    private Double revShareSum;

    public static GoogleMonthlyInvoiceSum toEntity(String year, String month, String revsCategory,
                                           Double productPriceSum, Double taxSum, Double totalAmountSum, Double revShareSum) {
        return GoogleMonthlyInvoiceSum.builder()
                .year(year)
                .month(month)
                .revsCategory(revsCategory)
                .productPriceSum(productPriceSum)
                .taxSum(taxSum)
                .totalAmountSum(totalAmountSum)
                .revShareSum(revShareSum)
                .build();
    }

    public static GoogleMonthlyInvoiceSum toRevsCategoryTotal(String year, String month) {
        return GoogleMonthlyInvoiceSum.builder()
                .year(year)
                .month(month)
                .revsCategory("SUM")
                .productPriceSum(0.0)
                .taxSum(0.0)
                .totalAmountSum(0.0)
                .revShareSum(0.0)
                .build();
    }

    public void addProductPriceSum(Double productPriceSum) {
        this.productPriceSum += productPriceSum;
    }
    public void addTaxSum(Double taxSum) {
        this.taxSum += taxSum;
    }
    public void addTotalAmountSum(Double totalAmountSum) {
        this.totalAmountSum += totalAmountSum;
    }
    public void addRevShareSum(Double revShareSum) {
        this.revShareSum += revShareSum;
    }
}
