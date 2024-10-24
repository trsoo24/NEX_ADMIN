package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertMerchantInfo {
    private String merchantNm;
    private String merchantContact;
    private String stdDt;
}
