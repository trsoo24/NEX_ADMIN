package com.example.admin.billing_grade.mapper;

import com.example.admin.billing_grade.dto.BillingGrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillingGradeMapper {
    List<BillingGrade> generateBillingGrade(Map<String, Object> map);
}
