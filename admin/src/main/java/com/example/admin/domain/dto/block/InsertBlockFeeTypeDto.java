package com.example.admin.domain.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBlockFeeTypeDto {
    private String feeTypeCd; // 요금제 코드
    private String mbrId;
    private String feeTypeNm; // 요금제명
    private String dcb;
}
