package com.example.admin.domain.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertMerchantDayStat {
    private String year;
    private String month;
    private String merchantName;
}
