package com.example.admin.product.dto;

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
public class ProductStatsDailyDto {
    private String sellerName;
    private String productName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> dailySales;

    public static ProductStatsDailyDto toProductStatDailyDto(ProductStatsDaily productStatsDaily) {
        return ProductStatsDailyDto.builder()
                .sellerName(productStatsDaily.getSellerName())
                .productName(productStatsDaily.getProductName())
                .total(0.0)
                .percent(0.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void addDailySales(ProductStatsDaily productStatsDaily) {
        this.dailySales.put(Integer.parseInt(productStatsDaily.getDay()), productStatsDaily.getSumTotal());
        this.total += productStatsDaily.getSumTotal();
    }

    public void addTotalDailySales(ProductStatsDaily productStatsDaily) {
        this.dailySales.compute(Integer.parseInt(productStatsDaily.getDay()), (k, value) -> (value == null ? 0 : value) + productStatsDaily.getSumTotal());
        this.total += productStatsDaily.getSumTotal();
    }

    public static ProductStatsDailyDto generateTotal() {
        return ProductStatsDailyDto.builder()
                .sellerName("합계")
                .productName("")
                .total(0.0)
                .percent(1.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}
