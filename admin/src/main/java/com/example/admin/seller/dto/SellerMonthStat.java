package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerMonthStat {
    private String year;
    private String month;
    private String sellerName;
    private Double sumPrice;
    private Double sumTax;
    private Double sumTotal;
    private final String dcb = "GDCB";

    public static SellerMonthStat toMonthStat(String year, String month, String sellerName, Double sumPrice, Double sumTax, Double sumTotal) {
        return SellerMonthStat.builder()
                .year(year)
                .month(month)
                .sellerName(sellerName)
                .sumPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
