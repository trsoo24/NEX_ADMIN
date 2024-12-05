package com.example.admin.policy.service;

import com.example.admin.policy.dto.PolicyInfo;
import com.example.admin.policy.dto.UpdatePolicyInfoDto;
import com.example.admin.policy.mapper.PolicyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
        String trxNo = MDC.get("trxNo");

        for (UpdatePolicyInfoDto dto1 : dto) {
            log.info("[{}] 요청 = 결제 정책 {} 정책 변경 요청", trxNo, dto1.getPolicyCode());
        }

        boolean updateResponse = policyMapper.updatePolicyInfo(dto);

        if (updateResponse) {
            log.info("[{}] 응답 = 결제 정책 {} 건 정책 변경 완료", trxNo, dto.size());
        } else {
            log.info("[{}] 응답 = 결제 정책 {} 건 정책 변경 실패", trxNo, dto.size());
        }
    }

    public List<PolicyInfo> selectAllPolicyInfo() {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 결제 정책 전체 조회", trxNo);

        List<PolicyInfo> infoList = policyMapper.selectAllPolicyInfo();

        log.info("[{}] 응답 = 결제 정책 {} 건 조회 완료", trxNo, infoList.size());

        return infoList;
    }
}
