package com.example.admin.controller;

import com.example.admin.domain.dto.range.RangeDayDto;
import com.example.admin.service.range.RangeDayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/range/day")
public class RangeDayController {
    private final RangeDayService rangeDayService;

    @PostMapping()
    public void insertRangeDay(@RequestParam("month") @Valid String month) {
        rangeDayService.insertRangeDay(month);
    }

    @GetMapping()
    public Map<String, List<RangeDayDto>> getRangeDayList(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        return rangeDayService.getRangeDayList(startDate, endDate, dcb);
    }
}
