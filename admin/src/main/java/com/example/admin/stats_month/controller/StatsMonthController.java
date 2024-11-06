package com.example.admin.stats_month.controller;

import com.example.admin.stats_month.dto.StatsMonth;
import com.example.admin.stats_month.service.StatsMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/month")
public class StatsMonthController {
    private final StatsMonthService statsMonthService;

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/raw")
    public List<StatsMonth> getDayPaymentsForScheduler() {
        return statsMonthService.getStatsMonthList();
    }
}
