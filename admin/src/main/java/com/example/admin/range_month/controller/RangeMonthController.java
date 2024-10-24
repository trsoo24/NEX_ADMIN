package com.example.admin.range_month.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.range_month.dto.RangeMonthDto;
import com.example.admin.range_month.dto.RangeMonth;
import com.example.admin.range_month.service.RangeMonthService;
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
@RequestMapping("/payment/range/month")
public class RangeMonthController {
    private final RangeMonthService rangeMonthService;

    @PostMapping()
    public void insertRangeMonth(@RequestParam("year") String year, @RequestParam("dcb") @Valid String dcb) {
        rangeMonthService.insertRangeMonth(year, dcb);
    }

    @GetMapping()
    public MapResult<String, List<RangeMonthDto>> getRangeMonthMap(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        Map<String, List<RangeMonthDto>> rangMonthDtoMap = rangeMonthService.getRangeMonthMap(startDate, endDate, dcbs);

        return new MapResult<>(true, rangMonthDtoMap);
    }

    @GetMapping("/excel")
    public void exportRangeMonthExcel(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeMonthService.exportExcel(startDate, endDate, dcbs, response);
    }

    // 통합 ADMIN 스케줄러 호출용 API
    @GetMapping("/2")
    public List<RangeMonth> getRangeMonthList(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("month") @Valid String month) {
        return rangeMonthService.getRangeMonth(month, dcbs);
    }
}
