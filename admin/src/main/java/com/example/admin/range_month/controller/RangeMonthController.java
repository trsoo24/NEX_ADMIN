package com.example.admin.range_month.controller;

import com.example.admin.range_month.dto.RangeMonth;
import com.example.admin.range_month.service.RangeMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/range/month")
public class RangeMonthController {
    private final RangeMonthService rangeMonthService;

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/raw")
    public List<RangeMonth> getRangeMonthList() {
        return rangeMonthService.getRangeMonth();
    }
}
