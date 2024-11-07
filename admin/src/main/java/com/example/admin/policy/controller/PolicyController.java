package com.example.admin.policy.controller;

import com.example.admin.policy.dto.PolicyInfo;
import com.example.admin.policy.dto.UpdatePolicyInfoDto;
import com.example.admin.policy.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policy")
@RequiredArgsConstructor
public class PolicyController {
    private final PolicyService policyService;

    @PutMapping
    public void updatePolicyInfo(@RequestBody @Valid List<UpdatePolicyInfoDto> dto) {
        policyService.updatePolicyInfo(dto);
    }

    @GetMapping
    public List<PolicyInfo> selectAllPolicyInfo() {
        return policyService.selectAllPolicyInfo();
    }
}
