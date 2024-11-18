package com.example.admin.payment_history.mapper;

import com.example.admin.payment_history.dto.PaymentHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentHistoryMapper {
    List<PaymentHistory> getPaymentHistoryList(Map<String, Object> map);
}
