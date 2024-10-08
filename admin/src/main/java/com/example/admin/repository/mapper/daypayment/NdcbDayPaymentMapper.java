package com.example.admin.repository.mapper.daypayment;

import com.example.admin.domain.entity.payment.DayPayment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NdcbDayPaymentMapper {
    void insertDayPayment(DayPayment dayPayment);
    List<DayPayment> getDayPaymentList(String date);
    DayPayment getDayPayment(String month);
}
