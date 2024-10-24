package com.example.admin.range_day.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.range_day.dto.RangeDayDto;
import com.example.admin.range_day.dto.RangeDay;
import com.example.admin.range_day.service.RangeDayService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/range/day")
public class RangeDayController {
    private final RangeDayService rangeDayService;

    @PostMapping()
    public StatusResult insertRangeDay(@RequestParam("month") @Valid String month, @RequestParam("dcb") @Valid String dcb) {
        rangeDayService.insertRangeDay(month, dcb);

        return new StatusResult(true);
    }

    @GetMapping()
    public MapResult<String, List<RangeDayDto>> getRangeDayMap(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        Map<String, List<RangeDayDto>> rangeDayDtoMap = rangeDayService.getRangeDayMap(startDate, endDate, dcbs);

        return new MapResult<>(true, rangeDayDtoMap);
    }

    @GetMapping("/2")
    public List<RangeDay> getRangeDayList(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("day") @Valid String day) {
        return rangeDayService.getRangeDay(day, dcbs);
    }

    @GetMapping("/excel")
    public void exportRangeDayExcel(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeDayService.exportExcel(startDate, endDate, dcbs, response);
    }
}
