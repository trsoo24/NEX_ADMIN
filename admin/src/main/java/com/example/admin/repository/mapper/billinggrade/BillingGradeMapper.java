package com.example.admin.repository.mapper.billinggrade;

import com.example.admin.domain.dto.billing.BillingGradeDto;
import com.example.admin.domain.entity.billing.BillingGrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillingGradeMapper {
    List<BillingGradeDto> getBillingGrade(Map<String, String> map);
    void insertBillingGrade(BillingGrade billingGrade);
}
