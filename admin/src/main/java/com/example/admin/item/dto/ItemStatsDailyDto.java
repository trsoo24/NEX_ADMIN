package com.example.admin.item.dto;

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
public class ItemStatsDailyDto {
    private String merchantName;
    private String itemName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> dailySales;

    public static ItemStatsDailyDto toItemStatDailyDto(ItemStatsDaily itemStatsDaily) {
        return ItemStatsDailyDto.builder()
                .merchantName(itemStatsDaily.getMerchantName())
                .itemName(itemStatsDaily.getItemName())
                .total(0.0)
                .percent(0.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void addDailySales(ItemStatsDaily itemStatsDaily) {
        this.dailySales.put(Integer.parseInt(itemStatsDaily.getDay()), itemStatsDaily.getSumTotal());
        this.total += itemStatsDaily.getSumTotal();
    }

    public void addTotalDailySales(ItemStatsDaily itemStatsDaily) {
        this.dailySales.compute(Integer.parseInt(itemStatsDaily.getDay()), (k, value) -> (value == null ? 0 : value) + itemStatsDaily.getSumTotal());
        this.total += itemStatsDaily.getSumTotal();
    }

    public static ItemStatsDailyDto generateTotal() {
        return ItemStatsDailyDto.builder()
                .merchantName("합계")
                .itemName("")
                .total(0.0)
                .percent(1.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}
