package com.example.admin.block.fee_type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockFeeTypeDto {
    private String feeTypeCd; // 요금제 코드
    private String regDt;
    private String mbrId;
    private String feeTypeNm; // 요금제명
    private String dcb;
}
