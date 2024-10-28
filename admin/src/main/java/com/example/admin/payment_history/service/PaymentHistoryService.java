package com.example.admin.payment_history.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.payment_history.dto.PaymentHistory;
import com.example.admin.payment_history.dto.PaymentHistoryDto;
import com.example.admin.payment_history.mapper.PaymentHistoryMapper;
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
public class PaymentHistoryService {
    private final PaymentHistoryMapper paymentHistoryMapper;
    private final FunctionUtil functionUtil;

    // 건별 상세 이력 조회 API 메서드
    public List<PaymentHistoryDto> getPaymentHistoryDtoPage(String startDate, String endDate, String ctn) {
        List<PaymentHistory> paymentHistoryList = getPaymentHistoryList(startDate, endDate, ctn);

        return getPaymentHistoryDtoList(paymentHistoryList);
    }

    public List<PaymentHistory> getPaymentHistoryList(String startDate, String endDate, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("startDate", functionUtil.monthToStartDate(startDate));
        requestMap.put("endDate", functionUtil.monthToEndDate(endDate));
        requestMap.put("ctn", ctn);

        return paymentHistoryMapper.getPaymentHistoryList(requestMap);
    }

    // 통합 ADMIN 에서 EXCEL 만들기 전에 API 호출
    public List<PaymentHistoryDto> getPaymentHistoryDtoList(List<PaymentHistory> paymentHistoryList) {
        List<PaymentHistoryDto> paymentHistoryDtoList = new ArrayList<>();

        for (PaymentHistory paymentHistory : paymentHistoryList) {
            paymentHistoryDtoList.add(PaymentHistoryDto.toDto(paymentHistory));
        }

        return paymentHistoryDtoList;
    }
}
