package com.example.admin.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {
    private Integer memberId;
    private String position;
    private String memberName;
    private String name;
    private String ctn;
    private String email;
    private String lastConnectedDateTime;
}
