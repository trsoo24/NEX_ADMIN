package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDayStat {
    private String year;
    private String month;
    private String day;
    private String sellerName;
    private Double sumPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static SellerDayStat to(String year, String month, String day, String sellerName, double sumPrice, double sumTax, double sumTotal) {
        return SellerDayStat.builder()
                .year(year)
                .month(month)
                .day(day)
                .sellerName(sellerName)
                .sumPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
