package com.example.admin.stats_day.controller;

import com.example.admin.stats_day.dto.StatsDay;
import com.example.admin.stats_day.service.StatsDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/day")
public class StatsDayController {
    private final StatsDayService statsDayService;

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/raw")
    public List<StatsDay> getDayPaymentsForScheduler() {
        return statsDayService.getStatsDayList();
    }
}
