package com.example.admin.range_day.controller;

import com.example.admin.range_day.dto.RangeDay;
import com.example.admin.range_day.service.RangeDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/range/day")
public class RangeDayController {
    private final RangeDayService rangeDayService;

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/raw")
    public List<RangeDay> getRangeDayList(@RequestParam("date") String date) {
        return rangeDayService.getRangeDay(date);
    }
}
