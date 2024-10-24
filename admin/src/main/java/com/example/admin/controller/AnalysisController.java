package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.analysis.DayAnalysisDto;
import com.example.admin.domain.dto.analysis.MonthAnalysisDto;
import com.example.admin.domain.entity.analysis.DayAnalysis;
import com.example.admin.service.analysis.DayAnalysisStatisticService;
import com.example.admin.service.analysis.MonthAnalysisStatisticsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final DayAnalysisStatisticService dayAnalysisStatisticService;
    private final MonthAnalysisStatisticsService monthAnalysisStatisticsService;

    // 통합 ADMIN 스케줄러 호출 API
    @GetMapping("/gdcb")
    public List<DayAnalysis> getAnalysisStatistics(@RequestParam("day") String day) {
        return dayAnalysisStatisticService.getAnalysisStatisticsList(day);
    }

    // 일별 통계 분석 ( 한 달 데이터까지만 조회 가능 )
    @GetMapping("/gdcb/day")
    public MapResult<String, List<DayAnalysisDto>> getDayAnalysisStatisticMap(@RequestParam("dcbs") List<String> dcbs,
                                                                              @RequestParam("startDate") String startDate,
                                                                              @RequestParam("endDate") String endDate,
                                                                              @RequestParam("codeName") String codeName) {
        Map<String, List<DayAnalysisDto>> analysisMap = dayAnalysisStatisticService.getDayAnalysisStatisticsMap(dcbs, startDate, endDate, codeName);

        return new MapResult<>(true, analysisMap);
    }

    @GetMapping("/gdcb/day/excel")
    public void exportDayAnalysisStatisticExcel(@RequestParam("dcbs") List<String> dcbs,
                                                @RequestParam("startDate") String startDate,
                                                @RequestParam("endDate") String endDate,
                                                @RequestParam("codeName") String codeName,
                                                HttpServletResponse response) throws IOException {
        dayAnalysisStatisticService.exportDayAnalysisStatisticExcel(dcbs, startDate, endDate, codeName, response);
    }

    // 테스트용 데이터
    @PostMapping("/gdcb/day")
    public StatusResult insertAnalysis(@RequestParam("year") String year, @RequestParam("dcb") String dcb) {
        dayAnalysisStatisticService.insertAnalysis(year, dcb);

        return new StatusResult(true);
    }

    // 월별 통계 분석 값 생성 ( 통합 ADMIN 스케줄러 )
    @GetMapping("/gdcb/month/add")
    public StatusResult insertAnalysisMonth(@RequestParam("month") String month) {
        monthAnalysisStatisticsService.insertMonthAnalysis(month);

        return new StatusResult(true);
    }

    @GetMapping("/gdcb/month")
    public MapResult<String, List<MonthAnalysisDto>> getMonthAnalysisStatisticMap(@RequestParam("dcbs") List<String> dcbs,
                                                                                  @RequestParam("startDate") String startDate,
                                                                                  @RequestParam("endDate") String endDate,
                                                                                  @RequestParam("codeName") String codeName) {
        Map<String, List<MonthAnalysisDto>> monthAnalysisMap = monthAnalysisStatisticsService.getMonthAnalysisStatisticsMap(dcbs, startDate, endDate, codeName);

        return new MapResult<>(true, monthAnalysisMap);
    }

    @GetMapping("/gdcb/month/excel")
    public void exportMonthAnalysisStatisticExcel(@RequestParam("dcbs") List<String> dcbs,
                                                  @RequestParam("startDate") String startDate,
                                                  @RequestParam("endDate") String endDate,
                                                  @RequestParam("codeName") String codeName,
                                                  HttpServletResponse response) throws IOException {
        monthAnalysisStatisticsService.exportMonthAnalysisStatisticExcel(dcbs, startDate, endDate, codeName, response);
    }
}
