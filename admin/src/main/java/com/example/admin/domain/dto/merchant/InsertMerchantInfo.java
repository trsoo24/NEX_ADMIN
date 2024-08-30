package com.example.admin.domain.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertMerchantInfo {
    private String merchantNm;
    private String merchantContact;
    private String stdDt;
}
