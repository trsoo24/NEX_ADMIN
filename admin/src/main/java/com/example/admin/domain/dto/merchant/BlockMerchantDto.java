package com.example.admin.domain.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockMerchantDto {
    private String dcb;
    private List<String> merchantNames;
}
