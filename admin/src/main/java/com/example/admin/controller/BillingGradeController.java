package com.example.admin.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.billing.BillingGradeDto;
import com.example.admin.domain.entity.billing.BillingGrade;
import com.example.admin.service.billing.BillingGradeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment/grade")
@RequiredArgsConstructor
public class BillingGradeController {
    private final BillingGradeService billingGradeService;

    @GetMapping
    public ListResult<BillingGradeDto> getBillingGradePage(@RequestParam("dcb") @Valid String dcb, @RequestParam("year") @Valid String year) { // 등급별 결제 현황 조회
        List<BillingGradeDto> dtoList = billingGradeService.searchBillingGrade(dcb, year);

        return new ListResult<>(true, dtoList);
    }

    @GetMapping("/2")
    public List<BillingGrade> getBillingGradeList(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month) { // 등급별 결제 현황 조회
        return billingGradeService.getBillingGrade(dcb, month);
    }


    @GetMapping("/excel")
    public void exportExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("year") @Valid String year, HttpServletResponse response) throws IOException {
        billingGradeService.exportBillingGradeExcel(dcb, year, response);
    }

    @PostMapping
    public void initRandomValue() {
        billingGradeService.insertRandomBillingGradeRecord("2024");
    }
}
