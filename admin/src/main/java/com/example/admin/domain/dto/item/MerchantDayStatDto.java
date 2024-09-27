package com.example.admin.domain.dto.item;

import com.example.admin.domain.entity.item.MerchantDayStat;
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
public class MerchantDayStatDto {
    private String merchantName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> dailySales;

    public static MerchantDayStatDto toItemDayStatDto(MerchantDayStat merchantDayStat) {
        return MerchantDayStatDto.builder()
                .merchantName(merchantDayStat.getMerchantName())
                .total(0.0)
                .percent(0.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void addDailySales(MerchantDayStat merchantDayStat) {
        this.dailySales.put(Integer.parseInt(merchantDayStat.getDay()), merchantDayStat.getSumTotal());
        this.total += merchantDayStat.getSumTotal();
    }

    public void addTotalDailySales(MerchantDayStat merchantDayStat) {
        this.dailySales.compute(Integer.parseInt(merchantDayStat.getDay()), (k, value) -> (value == null ? 0 : value) + merchantDayStat.getSumTotal());
        this.total += merchantDayStat.getSumTotal();
    }

    public static MerchantDayStatDto generateTotal() {
        return MerchantDayStatDto.builder()
                .merchantName("합계")
                .total(0.0)
                .percent(1.0)
                .dailySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = this.total / totalValue;
    }
}
