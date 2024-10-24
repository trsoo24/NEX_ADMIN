package com.example.admin.payment_day.mapper;

import com.example.admin.payment_day.dto.DayPayment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MdcbDayPaymentMapper {
    void insertDayPayment(DayPayment dayPayment);
    List<DayPayment> getDayPaymentList(String date);
    DayPayment getDayPayment(String month);
}
