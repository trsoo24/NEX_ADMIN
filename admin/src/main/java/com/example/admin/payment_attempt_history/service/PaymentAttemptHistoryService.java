package com.example.admin.payment_attempt_history.service;

import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistory;
import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistoryDto;
import com.example.admin.payment_attempt_history.mapper.PaymentAttemptHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        List<PaymentAttemptHistory> paymentAttemptHistoryList = getPaymentAttemptHistoryList(startDate, endDate, ctn);

        List<PaymentAttemptHistoryDto> paymentAttemptHistoryDtoList = new ArrayList<>();

        for (PaymentAttemptHistory paymentAttemptHistory : paymentAttemptHistoryList) {
            PaymentAttemptHistoryDto dto = PaymentAttemptHistoryDto.toDto(paymentAttemptHistory);

            paymentAttemptHistoryDtoList.add(dto);
        }

        return paymentAttemptHistoryDtoList;
    }
}
