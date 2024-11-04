package com.example.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertProductMonthStat {
    private String year;
    private String sellerName;
    private String productName;
}
