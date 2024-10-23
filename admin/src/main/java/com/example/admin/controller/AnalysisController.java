package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.analysis.DayAnalysisDto;
import com.example.admin.domain.entity.analysis.DayAnalysis;
import com.example.admin.service.analysis.AnalysisStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisStatisticService analysisStatisticService;

    // 통합 ADMIN 스케줄러 호출 API
    @GetMapping("/gdcb")
    public List<DayAnalysis> getAnalysisStatistics(@RequestParam("day") String day) {
        return analysisStatisticService.getAnalysisStatisticsList(day);
    }

    // 일별 통계 분석 ( 한 달 데이터까지만 조회 가능 )
    @GetMapping("/gdcb/day")
    public MapResult<String, List<DayAnalysisDto>> getDayAnalysisStatisticMap(@RequestParam("dcbs") List<String> dcbs,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate, @RequestParam("codeName") String codeName) {
        Map<String, List<DayAnalysisDto>> analysisMap = analysisStatisticService.getDayAnalysisStatisticsMap(dcbs, startDate, endDate, codeName);

        return new MapResult<>(true, analysisMap);
    }

    // 테스트용 데이터
    @PostMapping("/gdcb")
    public StatusResult insertAnalysis(@RequestParam("year") String year, @RequestParam("dcb") String dcb) {
        analysisStatisticService.insertAnalysis(year, dcb);

        return new StatusResult(true);
    }
}
