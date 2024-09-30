package com.example.admin.domain.entity.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemStatsDaily {
    private String year;
    private String month;
    private String day;
    private String merchantName;
    private String itemName;
    private Double sumItemPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static ItemStatsDaily toItemStatsDaily(String year, String month, String day, String merchantName, String itemName, double sumPrice, double sumTax, double sumTotal) {
        return ItemStatsDaily.builder()
                .year(year)
                .month(month)
                .day(day)
                .merchantName(merchantName)
                .itemName(itemName)
                .sumItemPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
