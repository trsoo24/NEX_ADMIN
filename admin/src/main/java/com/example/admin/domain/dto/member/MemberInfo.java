package com.example.admin.domain.dto.member;

import com.example.admin.domain.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfo {
    private Integer memberId;
    private String team;
    private List<String> services;
    private String username;
    private String name;
    private String email;
    private String lastLogin;
    private String locked;
    private String role;

    public MemberInfo toMemberInfo(Member member) {
        return MemberInfo.builder()
                .memberId(member.getMemberId())
                .team(member.getTeam())
                .services(generateServiceList(member))
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .lastLogin(member.getLastConnectedDateTime())
                .locked(member.getIsLocked())
                .role(member.getRole().getType())
                .build();
    }

    private List<String> generateServiceList(Member member) {
        List<String> services = new ArrayList<>();
        checkADCB(services, member.getADCB());
        checkGDCB(services, member.getGDCB());
        checkMDCB(services, member.getMDCB());
        checkMSDCB(services, member.getMSDCB());
        checkNDCB(services, member.getNDCB());
        checkPDCB(services, member.getPDCB());
        checkSDCB(services, member.getSDCB());

        return services;
    }

    private void checkADCB(List<String> dcbList, Boolean isADCB) {
        if (isADCB) {
            dcbList.add("ADCB");
        }
    }
    private void checkGDCB(List<String> dcbList, Boolean isGDCB) {
        if (isGDCB) {
            dcbList.add("GDCB");
        }
    }
    private void checkMDCB(List<String> dcbList, Boolean isMDCB) {
        if (isMDCB) {
            dcbList.add("MDCB");
        }
    }
    private void checkMSDCB(List<String> dcbList, Boolean isMSDCB) {
        if (isMSDCB) {
            dcbList.add("MSDCB");
        }
    }
    private void checkNDCB(List<String> dcbList, Boolean isNDCB) {
        if (isNDCB) {
            dcbList.add("NDCB");
        }
    }
    private void checkPDCB(List<String> dcbList, Boolean isPDCB) {
        if (isPDCB) {
            dcbList.add("PDCB");
        }
    }
    private void checkSDCB(List<String> dcbList, Boolean isSDCB) {
        if (isSDCB) {
            dcbList.add("SDCB");
        }
    }
}
