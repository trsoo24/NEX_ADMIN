package com.example.admin.billing_grade.service;

import com.example.admin.billing_grade.dto.BillingGrade;
import com.example.admin.billing_grade.mapper.BillingGradeMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingGradeService {
    private final BillingGradeMapper billingGradeMapper;

    public List<BillingGrade> generateBillingGradeList() {
        BillingGrade billingGrade = new BillingGrade();
        List<BillingGrade> billingGradeList = new ArrayList<>();

        log.info("---------------------------- GDCB GRADE BILLING START ----------------------------");

        List<String> lastMonthList = FunctionUtil.getlastMonthTryList();

//        if(!lastMonthList.isEmpty()) { // 지난 달 누락 시 재실행
//            checkPreviousMonthBillingGrade(lastMonthList);
//        }

        try {
            // 고객한도별 월 단위 청구현황 가져오기
            billingGradeList = billingGradeMapper.generateBillingGrade(billingGrade);
            log.info("SELECT RESULT: " + billingGradeList);

            // 고객한도별 월 단위 청구현황 저장
            if(billingGradeList != null) {
                for(BillingGrade bg : billingGradeList) {
                    log.info("INSERT SUCCESS: " + bg);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            log.info("---------------------------- GDCB GRADE BILLING END ----------------------------");
        }

        return billingGradeList;
    }
}
