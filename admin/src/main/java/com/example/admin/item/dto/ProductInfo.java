package com.example.admin.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private String productName;
    private String sellerCompany;
    private String stdDt;
}