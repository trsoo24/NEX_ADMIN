package com.example.admin.domain.entity.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemMonthStat {
    private Integer year;
    private Integer month;
    private String merchantName;
    private String itemName;
    private Double sumItemPrice;
    private Double sumTax;
    private Double sumTotal;
}
