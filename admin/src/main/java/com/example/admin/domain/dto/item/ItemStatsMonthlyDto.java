package com.example.admin.domain.dto.item;

import com.example.admin.domain.entity.item.ItemStatsMonthly;
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
public class ItemStatsMonthlyDto {
    private String merchantName;
    private String itemName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> monthlySales;

    public static ItemStatsMonthlyDto toItemStatMonthlyDto(ItemStatsMonthly itemStatsMonthly) {
        return ItemStatsMonthlyDto.builder()
                .merchantName(itemStatsMonthly.getMerchantName())
                .itemName(itemStatsMonthly.getItemName())
                .total(0.0)
                .percent(0.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void addMonthlySales(ItemStatsMonthly itemStatsMonthly) {
        this.monthlySales.put(Integer.parseInt(itemStatsMonthly.getMonth()), itemStatsMonthly.getSumTotal());
        this.total += itemStatsMonthly.getSumTotal();
    }

    public void addTotalMonthlySales(ItemStatsMonthly itemStatsMonthly) {
        this.monthlySales.compute(Integer.parseInt(itemStatsMonthly.getMonth()), (k, value) -> (value == null ? 0 : value) + itemStatsMonthly.getSumTotal());
        this.total += itemStatsMonthly.getSumTotal();
    }

    public static ItemStatsMonthlyDto generateTotal() {
        return ItemStatsMonthlyDto.builder()
                .merchantName("합계")
                .itemName("")
                .total(0.0)
                .percent(1.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}