package com.example.admin.domain.entity.member;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private int memberId;
    private String position;
    private String memberName;
    private String password;
    private String name;
    private String ctn;
    private String email;
    private String lastConnectedDateTime;
}
