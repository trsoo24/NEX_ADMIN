package com.example.admin.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemStatsMonthly {
    private String year;
    private String month;
    private String merchantName;
    private String itemName;
    private Double sumItemPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static ItemStatsMonthly toItemStatsMonthly(String year, String month, String merchantName, String itemName, double sumItemPrice, double sumTax, double sumTotal) {
        return ItemStatsMonthly.builder()
                .year(year)
                .month(month)
                .merchantName(merchantName)
                .itemName(itemName)
                .sumItemPrice(sumItemPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}