package com.example.admin.analysis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetDayAnalysisDto {
    private String startDate;
    private String endDate;

    public GetDayAnalysisDto(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
