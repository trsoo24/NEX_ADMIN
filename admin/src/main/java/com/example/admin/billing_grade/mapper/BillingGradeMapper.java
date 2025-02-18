package com.example.admin.billing_grade.mapper;

import com.example.admin.billing_grade.dto.BillingGrade;
import com.example.admin.billing_grade.dto.GetBillingGradeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BillingGradeMapper {
    List<BillingGrade> generateBillingGrade(GetBillingGradeDto dto);
}
