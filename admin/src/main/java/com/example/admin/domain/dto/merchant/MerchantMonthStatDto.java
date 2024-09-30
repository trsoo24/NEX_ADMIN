package com.example.admin.domain.dto.merchant;

import com.example.admin.domain.entity.merchant.MerchantMonthStat;
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
public class MerchantMonthStatDto {
    private String merchantName;
    private Double total;
    private Double percent;
    private Map<Integer, Double> monthlySales;

    public static MerchantMonthStatDto toMerchantMonthStatDto(MerchantMonthStat merchantMonthStat) {
        return MerchantMonthStatDto.builder()
                .merchantName(merchantMonthStat.getMerchantName())
                .total(0.0)
                .percent(0.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void addMonthlySales(MerchantMonthStat merchantMonthStat) {
        this.monthlySales.put(Integer.valueOf(merchantMonthStat.getMonth()), merchantMonthStat.getSumTotal());
        this.total += merchantMonthStat.getSumTotal();
    }

    public void addTotalMonthlySales(MerchantMonthStat merchantMonthStat) {
        this.monthlySales.compute(Integer.valueOf(merchantMonthStat.getMonth()), (k, value) -> (value == null ? 0 : value) + merchantMonthStat.getSumTotal());
        this.total += merchantMonthStat.getSumTotal();
    }

    public static MerchantMonthStatDto generateTotal() {
        return MerchantMonthStatDto.builder()
                .merchantName("합계")
                .total(0.0)
                .percent(1.0)
                .monthlySales(new LinkedHashMap<>())
                .build();
    }

    public void setPercent(Double totalValue) {
        this.percent = Math.round(this.total / totalValue * 100.0) / 100.0;
    }
}
