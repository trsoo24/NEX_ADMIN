package com.example.admin.block.fee_type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockFeeType {
    private int feeTypeId;
    private String feeTypeCd; // 요금제 코드
    private String regDt;
    private String mbrId;
    private String feeTypeNm; // 요금제명
}
