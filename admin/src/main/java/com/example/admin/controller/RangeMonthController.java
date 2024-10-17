package com.example.admin.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.range.RangeMonthDto;
import com.example.admin.domain.entity.range.RangeMonth;
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
    public void insertRangeMonth(@RequestParam("year") String year, @RequestParam("dcb") @Valid String dcb) {
        rangeMonthService.insertRangeMonth(year, dcb);
    }

    @GetMapping()
    public MapResult<String, List<RangeMonthDto>> getRangeMonthMap(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate) throws IllegalAccessException {
        Map<String, List<RangeMonthDto>> rangMonthDtoMap = rangeMonthService.getRangeMonthMap(startDate, endDate, dcb);

        return new MapResult<>(true, rangMonthDtoMap);
    }

    @GetMapping("/2")
    public List<RangeMonth> getRangeMonthList(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month) {
        return rangeMonthService.getRangeMonthList(month, dcb);
    }

    @GetMapping("/excel")
    public void exportRangeMonthExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeMonthService.exportExcel(startDate, endDate, dcb, response);
    }
}
