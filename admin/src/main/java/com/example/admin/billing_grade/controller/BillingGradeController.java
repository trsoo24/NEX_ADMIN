package com.example.admin.billing_grade.controller;

import com.example.admin.billing_grade.dto.BillingGrade;
import com.example.admin.billing_grade.service.BillingGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment/grade")
@RequiredArgsConstructor
public class BillingGradeController {
    private final BillingGradeService billingGradeService;

    @GetMapping("/raw")
    public List<BillingGrade> getBillingGradeListForScheduler(@RequestParam("yyyyMm") String yyyyMm) {
        return billingGradeService.generateBillingGradeList(yyyyMm);
    }
}
