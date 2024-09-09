package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.range.RangeMonthDto;
import com.example.admin.service.range.RangeMonthService;
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
    public void insertRangeMonth(@RequestParam("year") String year) {
        rangeMonthService.insertRangeMonth(year);
    }

    @GetMapping()
    public MapResult<String, List<RangeMonthDto>> getRangeMonth(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        Map<String, List<RangeMonthDto>> rangMonthDtoMap = rangeMonthService.getRangeMonthList(startDate, endDate, dcb);

        return new MapResult<>(true, rangMonthDtoMap);
    }

    @GetMapping("/excel")
    public StatusResult exportRangeMonthExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeMonthService.exportExcel(startDate, endDate, dcb, response);

        return new StatusResult(true);
    }
}
