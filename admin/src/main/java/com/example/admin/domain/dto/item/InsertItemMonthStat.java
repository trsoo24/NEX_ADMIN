package com.example.admin.domain.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertItemMonthStat {
    private String year;
    private String merchantName;
    private String itemName;
}