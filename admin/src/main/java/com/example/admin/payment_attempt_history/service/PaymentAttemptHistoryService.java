package com.example.admin.payment_attempt_history.service;

import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistory;
import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistoryDto;
import com.example.admin.payment_attempt_history.mapper.PaymentAttemptHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAttemptHistoryService {
    private final PaymentAttemptHistoryMapper paymentAttemptHistoryMapper;

    // 결제 시도 이력 조회 API
    public List<PaymentAttemptHistory> getPaymentAttemptHistoryList(String startDate, String endDate, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("ctn", ctn);

        return paymentAttemptHistoryMapper.getPaymentAttemptHistoryList(requestMap);
    }

    public List<PaymentAttemptHistoryDto> getPaymentAttemptHistoryDtoList(String startDate, String endDate, String ctn) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = {} 부터 {} 까지 CTN = {} 결제 시도 이력 조회", trxNo, startDate, endDate, ctn);

        List<PaymentAttemptHistory> paymentAttemptHistoryList = getPaymentAttemptHistoryList(startDate, endDate, ctn);

        List<PaymentAttemptHistoryDto> paymentAttemptHistoryDtoList = new ArrayList<>();

        for (PaymentAttemptHistory paymentAttemptHistory : paymentAttemptHistoryList) {
            PaymentAttemptHistoryDto dto = PaymentAttemptHistoryDto.toDto(paymentAttemptHistory);

            paymentAttemptHistoryDtoList.add(dto);
        }

        log.info("[{}] 응답 = 결제 시도 이력 {} 건 조회 완료", trxNo, paymentAttemptHistoryList.size());

        return paymentAttemptHistoryDtoList;
    }
}
