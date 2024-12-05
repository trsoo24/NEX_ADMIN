package com.example.admin.payment_history.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.payment_history.dto.PaymentHistory;
import com.example.admin.payment_history.dto.PaymentHistoryDto;
import com.example.admin.payment_history.mapper.PaymentHistoryMapper;
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
public class PaymentHistoryService {
    private final PaymentHistoryMapper paymentHistoryMapper;

    // 건별 상세 이력 조회 API 메서드
    public List<PaymentHistoryDto> getPaymentHistoryDtoList(String startDate, String endDate, String ctn) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = {} 부터 {} 까지 CTN = {} 결제 상세 이력 조회", trxNo, startDate, endDate, ctn);

        List<PaymentHistory> paymentHistoryList = getPaymentHistoryList(startDate, endDate, ctn);

        log.info("[{}] 응답 = 결제 상세 이력 {} 건 조회 완료", trxNo, paymentHistoryList.size());

        return getPaymentHistoryDtoList(paymentHistoryList);
    }

    public List<PaymentHistory> getPaymentHistoryList(String startDate, String endDate, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        startDate = FunctionUtil.monthStartTransToYYYYmmDDHH24MISS(startDate);
        endDate = FunctionUtil.montEndTransToYYYYmmDDHH24MISS(endDate);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("ctn", ctn);

        List<PaymentHistory> response = paymentHistoryMapper.getPaymentHistoryList(requestMap);

        return response;
    }

    public List<PaymentHistoryDto> getPaymentHistoryDtoList(List<PaymentHistory> paymentHistoryList) {
        List<PaymentHistoryDto> paymentHistoryDtoList = new ArrayList<>();

        for (PaymentHistory paymentHistory : paymentHistoryList) {
            paymentHistoryDtoList.add(PaymentHistoryDto.toDto(paymentHistory));
        }

        return paymentHistoryDtoList;
    }
}