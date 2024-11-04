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
public class ProductStatsMonthlyDto {
    private String sellerName;
    private String productName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> monthlySales;

    public static ProductStatsMonthlyDto toProductStatMonthlyDto(ProductStatsMonthly productStatsMonthly) {
        return ProductStatsMonthlyDto.builder()
                .sellerName(productStatsMonthly.getSellerName())
                .productName(productStatsMonthly.getProductName())
                .total(0.0)
                .percent(0.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void addMonthlySales(ProductStatsMonthly productStatsMonthly) {
        this.monthlySales.put(Integer.parseInt(productStatsMonthly.getMonth()), productStatsMonthly.getSumTotal());
        this.total += productStatsMonthly.getSumTotal();
    }

    public void addTotalMonthlySales(ProductStatsMonthly productStatsMonthly) {
        this.monthlySales.compute(Integer.parseInt(productStatsMonthly.getMonth()), (k, value) -> (value == null ? 0 : value) + productStatsMonthly.getSumTotal());
        this.total += productStatsMonthly.getSumTotal();
    }

    public static ProductStatsMonthlyDto generateTotal() {
        return ProductStatsMonthlyDto.builder()
                .sellerName("합계")
                .productName("")
                .total(0.0)
                .percent(1.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}