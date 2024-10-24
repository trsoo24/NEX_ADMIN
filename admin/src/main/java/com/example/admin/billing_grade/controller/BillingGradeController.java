package com.example.admin.billing_grade.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.billing_grade.dto.BillingGradeDto;
import com.example.admin.billing_grade.service.BillingGradeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment/grade")
@RequiredArgsConstructor
public class BillingGradeController {
    private final BillingGradeService billingGradeService;

    @GetMapping
    public MapResult<String, List<BillingGradeDto>> getBillingGradePage(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("year") @Valid String year) { // 등급별 결제 현황 조회
        Map<String, List<BillingGradeDto>> dtoMap = billingGradeService.searchBillingGrade(dcbs, year);

        return new MapResult<>(true, dtoMap);
    }

    @GetMapping("/excel")
    public void exportExcel(@RequestParam("dcbs") @Valid List<String> dcbs, @RequestParam("year") @Valid String year, HttpServletResponse response) throws IOException {
        billingGradeService.exportBillingGradeExcel(dcbs, year, response);
    }

    @PostMapping
    public void initRandomValue() {
        billingGradeService.insertRandomBillingGradeRecord("2024");
    }
}
