package com.example.admin.policy.mapper;

import com.example.admin.policy.dto.PolicyInfo;
import com.example.admin.policy.dto.UpdatePolicyInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyMapper {
    boolean updatePolicyInfo(List<UpdatePolicyInfoDto> dtoList);
    List<PolicyInfo> selectAllPolicyInfo();
}
