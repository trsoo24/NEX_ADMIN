package com.example.admin.type_limit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTypeLimitDto {
    private String elapseMonth;
    private String feeTypeCode1;
    private String feeTypeName1;
    private Long limitOnce1;
    private Long limitTotal1;
    private String feeTypeCode2;
    private String feeTypeName2;
    private Long limitOnce2;
    private Long limitTotal2;
}
