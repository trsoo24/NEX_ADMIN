package com.example.admin.policy.service;

import com.example.admin.policy.dto.PolicyInfo;
import com.example.admin.policy.dto.UpdatePolicyInfoDto;
import com.example.admin.policy.mapper.PolicyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyMapper policyMapper;

    @Transactional
    public void updatePolicyInfo(List<UpdatePolicyInfoDto> dto) {
        policyMapper.updatePolicyInfo(dto);
    }

    public List<PolicyInfo> selectAllPolicyInfo() {
        return policyMapper.selectAllPolicyInfo();
    }
}
