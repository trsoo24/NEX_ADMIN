package com.example.admin.controller;

import com.example.admin.service.analysis.AnalysisStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisStatisticService analysisStatisticService;

    @Scheduled(cron = "0 0 1 1 * *")
    public void insertAnalysisStaticsData() {
        analysisStatisticService.insertAnalysisStaticsData();
    }
}
