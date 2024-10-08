package com.example.admin.repository.mapper.monthpayment;

import com.example.admin.domain.entity.payment.MonthPayment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdcbMonthPaymentMapper {
    void insertMonthPayment(MonthPayment monthPayment);

    List<MonthPayment> getMonthPaymentList(String year);
    MonthPayment getMonthPayment(String year);
}
