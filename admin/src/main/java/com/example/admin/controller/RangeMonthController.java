package com.example.admin.controller;

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
    public Map<String, List<RangeMonthDto>> getRangeMonth(@RequestParam("startDate") @Valid String startDate, @RequestParam("endDate") @Valid String endDate, @RequestParam("dcb") @Valid String dcb) throws IllegalAccessException {
        return rangeMonthService.getRangeMonthList(startDate, endDate, dcb);
    }

    @GetMapping("/excel")
    public void exportRangeMonthExcel(@RequestParam("startDate")@Valid String startDate, @RequestParam("endDate")@Valid String endDate, @RequestParam("dcb") @Valid String dcb, HttpServletResponse response) throws IllegalAccessException, IOException, NoSuchFieldException {
        rangeMonthService.exportExcel(startDate, endDate, dcb, response);
    }
}
