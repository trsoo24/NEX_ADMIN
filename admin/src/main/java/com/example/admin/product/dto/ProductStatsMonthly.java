package com.example.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatsMonthly {
    private String year;
    private String month;
    private String sellerName;
    private String productName;
    private Double sumProductPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static ProductStatsMonthly toProductStatsMonthly(String year, String month, String sellerName, String productName, double sumProductPrice, double sumTax, double sumTotal) {
        return ProductStatsMonthly.builder()
                .year(year)
                .month(month)
                .sellerName(sellerName)
                .productName(productName)
                .sumProductPrice(sumProductPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}