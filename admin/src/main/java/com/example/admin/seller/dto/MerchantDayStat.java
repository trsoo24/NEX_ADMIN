package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantDayStat {
    private String year;
    private String month;
    private String day;
    private String merchantName;
    private Double sumPrice; // 판매액 합계
    private Double sumTax; // 세금 합계
    private Double sumTotal; // 결제 금액 합계

    public static MerchantDayStat to(String year, String month, String day, String merchantName, double sumPrice, double sumTax, double sumTotal) {
        return MerchantDayStat.builder()
                .year(year)
                .month(month)
                .day(day)
                .merchantName(merchantName)
                .sumPrice(sumPrice)
                .sumTax(sumTax)
                .sumTotal(sumTotal)
                .build();
    }
}
