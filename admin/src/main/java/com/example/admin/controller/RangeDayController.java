package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.range.RangeDayDto;
import com.example.admin.service.range.RangeDayService;
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
    public StatusResult insertRangeDay(@RequestParam("month") @Valid String month) {
        rangeDayService.insertRangeDay(month);

        return new StatusResult(true);
    }

    @GetMapping()
    public MapResult<String, List<RangeDayDto>> getRangeDayList(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        Map<String, List<RangeDayDto>> rangeDayDtoMap = rangeDayService.getRangeDayList(startDate, endDate, dcb);

        return new MapResult<>(true, rangeDayDtoMap);
    }

    @GetMapping("/excel")
    public StatusResult exportRangeDayExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeDayService.exportExcel(startDate, endDate, dcb, response);

        return new StatusResult(true);
    }
}
