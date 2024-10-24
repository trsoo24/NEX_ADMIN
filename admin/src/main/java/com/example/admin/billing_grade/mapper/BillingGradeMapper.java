package com.example.admin.billing_grade.mapper;

import com.example.admin.billing_grade.dto.BillingGradeDto;
import com.example.admin.billing_grade.dto.BillingGrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillingGradeMapper {
    List<BillingGradeDto> getBillingGradeDto(Map<String, Object> map);
    void insertBillingGrade(BillingGrade billingGrade);
    List<BillingGrade> getBillingGrade(Map<String, Object> map);
}
