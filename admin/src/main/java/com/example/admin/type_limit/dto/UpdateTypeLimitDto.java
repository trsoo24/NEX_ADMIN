package com.example.admin.type_limit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTypeLimitDto {
    private String elapseMonth;
    private Long limitOnce;
    private Long limitTotal;
    private String feeTypeCode;
}
