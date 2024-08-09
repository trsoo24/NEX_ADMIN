package com.example.admin.repository.mapper.billinggrade;

import com.example.admin.domain.dto.billing.BillingGradeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BillingGradeMapper {
    List<BillingGradeDto> getBillingGradeInfo();
}
