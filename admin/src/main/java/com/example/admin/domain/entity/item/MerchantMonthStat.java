package com.example.admin.domain.entity.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantMonthStat {
    private String year;
    private String month;
    private String merchantName;
    private Double sumPrice;
    private Double sumTax;
    private Double sumTotal;

    public static MerchantMonthStat toMonthStat(String year, String month, String merchantName, Double sumPrice, Double sumTax, Double sumTotal) {
        return MerchantMonthStat.builder()
                .year(year)
                .month(month)
                .merchantName(merchantName)
                .sumPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
