package com.example.admin.payment_attempt_history.mapper;

import com.example.admin.payment_attempt_history.dto.PaymentAttemptHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentAttemptHistoryMapper {
    List<PaymentAttemptHistory> getPaymentAttemptHistoryList(Map<String, Object> map);
}
