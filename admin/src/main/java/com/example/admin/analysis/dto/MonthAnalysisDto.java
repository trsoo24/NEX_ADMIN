package com.example.admin.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthAnalysisDto {
    private String createMonth;
    private Map<String, Long> resultCodeMap; // 기타도 같이 담기

    public static MonthAnalysisDto toDto(MonthAnalysis monthAnalysis) {
        return MonthAnalysisDto.builder()
                .createMonth(monthAnalysis.getCreateMonth())
                .resultCodeMap(new LinkedHashMap<>())
                .build();
    }

    public void addCount(MonthAnalysis monthAnalysis) {
        this.resultCodeMap.put(monthAnalysis.getResultCode(), getResultCodeMap().getOrDefault(monthAnalysis.getResultCode(), (long) 0) + monthAnalysis.getCodeCount());
    }

    public void addEtcCount(long codeCount) {
        String key = "기타";

        this.resultCodeMap.put(key, this.resultCodeMap.getOrDefault(key, (long) 0) + codeCount);
    }

    public void setResultCodeMap(Map<String, Long> resultCodeMap) {
        this.resultCodeMap = resultCodeMap;
    }
}
