package com.example.admin.block.fee_type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFeeTypeDto {
    private String dcb;
    private List<String> feeTypeCodes;
}
