package com.example.admin.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertItemDayStat {
    private String year;
    private String month;
    private String merchantName;
    private String itemName;
}
