package com.example.admin.domain.dto.analysis;

import com.example.admin.domain.entity.analysis.DayAnalysis;
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
public class DayAnalysisDto {
    private String createDt;
    private Map<String, Integer> resultCodeMap; // 기타도 같이 담기

    public static DayAnalysisDto toDto(DayAnalysis dayAnalysis) {
        return DayAnalysisDto.builder()
                .createDt(dayAnalysis.getCreateDt())
                .resultCodeMap(new LinkedHashMap<>())
                .build();
    }

    public void addCount(DayAnalysis dayAnalysis) {
        this.resultCodeMap.put(dayAnalysis.getResultCode(), getResultCodeMap().getOrDefault(dayAnalysis.getResultCode(), 0) + dayAnalysis.getCodeCount());
    }

    public void addEtcCount(int codeCount) {
        String key = "기타";

        this.resultCodeMap.put(key, this.resultCodeMap.getOrDefault(key, 0) + codeCount);
    }

    public void setResultCodeMap(Map<String, Integer> resultCodeMap) {
        this.resultCodeMap = resultCodeMap;
    }
}
