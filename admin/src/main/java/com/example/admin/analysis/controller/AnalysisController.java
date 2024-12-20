package com.example.admin.analysis.controller;

import com.example.admin.analysis.dto.DayAnalysis;
import com.example.admin.analysis.service.DayAnalysisStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final DayAnalysisStatisticService dayAnalysisStatisticService;

    // 통합 ADMIN 스케줄러 호출 API
    @GetMapping("/payment/day/raw")
    public List<DayAnalysis> getAnalysisStatistics(@RequestParam("date") String date) {
        return dayAnalysisStatisticService.getAnalysisStatisticsList(date);
    }
}
