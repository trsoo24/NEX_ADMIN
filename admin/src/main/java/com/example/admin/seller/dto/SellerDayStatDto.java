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
public class SellerDayStatDto {
    private String sellerName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> dailySales;

    public static SellerDayStatDto toSellerDayStatDto(SellerDayStat sellerDayStat) {
        return SellerDayStatDto.builder()
                .sellerName(sellerDayStat.getSellerName())
                .total(0.0)
                .percent(0.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void addDailySales(SellerDayStat sellerDayStat) {
        this.dailySales.put(Integer.parseInt(sellerDayStat.getDay()), sellerDayStat.getSumTotal());
        this.total += sellerDayStat.getSumTotal();
    }

    public void addTotalDailySales(SellerDayStat sellerDayStat) {
        this.dailySales.compute(Integer.parseInt(sellerDayStat.getDay()), (k, value) -> (value == null ? 0 : value) + sellerDayStat.getSumTotal());
        this.total += sellerDayStat.getSumTotal();
    }

    public static SellerDayStatDto generateTotal() {
        return SellerDayStatDto.builder()
                .sellerName("합계")
                .total(0.0)
                .percent(1.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}
