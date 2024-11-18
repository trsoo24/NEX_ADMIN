package com.example.admin.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayAnalysis {
    private String createDt;
    private String resultCode;
    private Integer codeCount;
    private String dcb;
}
