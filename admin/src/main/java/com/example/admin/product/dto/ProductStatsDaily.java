package com.example.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatsDaily {
    private String year;
    private String month;
    private String day;
    private String sellerName;
    private String productName;
    private Double sumProductPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static ProductStatsDaily toProductStatsDaily(String year, String month, String day, String sellerName, String productName, double sumPrice, double sumTax, double sumTotal) {
        return ProductStatsDaily.builder()
                .year(year)
                .month(month)
                .day(day)
                .sellerName(sellerName)
                .productName(productName)
                .sumProductPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
