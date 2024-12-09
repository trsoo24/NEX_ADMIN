package com.example.admin.billing_grade.service;

import com.example.admin.billing_grade.dto.BillingGrade;
import com.example.admin.billing_grade.mapper.BillingGradeMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingGradeService {
    private final BillingGradeMapper billingGradeMapper;

    public List<BillingGrade> generateBillingGradeList() {
        String trxNo = MDC.get("trxNo");

        BillingGrade billingGrade = new BillingGrade();
        List<BillingGrade> billingGradeList = new ArrayList<>();

        log.info("[{}] 요청 = {} 부터 {} 까지 월 등급별 결제 현황 종합", trxNo, formatDateForLog(billingGrade.getFirstDay()), formatDateForLog(billingGrade.getLastDay()));

        List<String> lastMonthList = FunctionUtil.getlastMonthTryList();

//        if(!lastMonthList.isEmpty()) { // 지난 달 누락 시 재실행
//            checkPreviousMonthBillingGrade(lastMonthList);
//        }

        try {
            // 고객한도별 월 단위 청구현황 가져오기
            log.info("resultCode : {}, firstDay : {}, lastDay : {}, api_type1 :{}, api_type2 :{}, api_type3 :{}, paid : {}, unpaid : {}",
                    billingGrade.getResultCode(), billingGrade.getFirstDay(), billingGrade.getLastDay(), billingGrade.getApi_type1(), billingGrade.getApi_type2(), billingGrade.getApi_type3(), billingGrade.getPaid(), billingGrade.getUnpaid());
            billingGradeList = billingGradeMapper.generateBillingGrade(billingGrade);

            log.info("[{}] 응답 데이터 = GDCB 월 등급별 결제 현황 {} 건 호출", trxNo, billingGradeList.size());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return billingGradeList;
    }

    private String formatDateForLog(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 입력 문자열을 LocalDateTime으로 변환
        LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);

        // 원하는 형식으로 변환
        return dateTime.format(outputFormatter);
    }
}
