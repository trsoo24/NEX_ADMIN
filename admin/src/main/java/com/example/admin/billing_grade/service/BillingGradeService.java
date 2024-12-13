package com.example.admin.billing_grade.service;

import com.example.admin.billing_grade.dto.BillingGrade;
import com.example.admin.billing_grade.dto.GetBillingGradeDto;
import com.example.admin.billing_grade.dto.type.BillingGradeResultCode;
import com.example.admin.billing_grade.mapper.BillingGradeMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

        Map<String, Object> requestMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String statYyMm = new SimpleDateFormat("yyyyMM").format(cal.getTime());
        String resultCode = BillingGradeResultCode.SUCCESS.getValue();
        String firstDay = statYyMm + "01000000";
        String lastDay = statYyMm + cal.getActualMaximum(Calendar.DAY_OF_MONTH) +"235959";
        String charge = "B";
        String reversal = "C";
        String refund = "R";
        String paid = "N";
        String unpaid = "Y";

        GetBillingGradeDto dto = new GetBillingGradeDto(resultCode, firstDay, lastDay, charge, reversal, refund, paid, unpaid);

        List<BillingGrade> billingGradeList = new ArrayList<>();

        log.info("[{}] 요청 = {} 부터 {} 까지 월 등급별 결제 현황 종합", trxNo, formatDateForLog(firstDay), formatDateForLog(lastDay));

//        List<String> lastMonthList = FunctionUtil.getlastMonthTryList();

//        if(!lastMonthList.isEmpty()) { // 지난 달 누락 시 재실행
//            checkPreviousMonthBillingGrade(lastMonthList);
//        }

        try {
            // 고객한도별 월 단위 청구현황 가져오기
            billingGradeList = billingGradeMapper.generateBillingGrade(dto);

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
