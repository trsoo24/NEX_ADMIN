package com.example.admin.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthAnalysis {
    private String createMonth;
    private String resultCode;
    private Long codeCount;
    private String dcb;

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
