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
    private String statDay;
    private String resultCode;
    private Integer codeCount;
    private final String dcb = "GDCB";
}
