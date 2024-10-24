package com.example.admin.payment_month.mapper;

import com.example.admin.payment_month.dto.MonthPayment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GdcbMonthPaymentMapper {
    void insertMonthPayment(MonthPayment monthPayment);

    List<MonthPayment> getMonthPaymentList(String year);
    MonthPayment getMonthPayment(String year);
}
