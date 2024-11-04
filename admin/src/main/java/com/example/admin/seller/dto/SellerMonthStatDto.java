package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerMonthStatDto {
    private String sellerName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> monthlySales;

    public static SellerMonthStatDto toSellerMonthStatDto(SellerMonthStat sellerMonthStat) {
        return SellerMonthStatDto.builder()
                .sellerName(sellerMonthStat.getSellerName())
                .total(0.0)
                .percent(0.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void addMonthlySales(SellerMonthStat sellerMonthStat) {
        this.monthlySales.put(Integer.valueOf(sellerMonthStat.getMonth()), sellerMonthStat.getSumTotal());
        this.total += sellerMonthStat.getSumTotal();
    }

    public void addTotalMonthlySales(SellerMonthStat sellerMonthStat) {
        this.monthlySales.compute(Integer.valueOf(sellerMonthStat.getMonth()), (k, value) -> (value == null ? 0 : value) + sellerMonthStat.getSumTotal());
        this.total += sellerMonthStat.getSumTotal();
    }

    public static SellerMonthStatDto generateTotal() {
        return SellerMonthStatDto.builder()
                .sellerName("합계")
                .total(0.0)
                .percent(1.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}
