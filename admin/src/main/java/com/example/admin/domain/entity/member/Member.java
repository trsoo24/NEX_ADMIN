package com.example.admin.domain.entity.member;

import com.example.admin.domain.entity.member.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private int memberId;
    private String team;
    private String username;
    private String password;
    private String name;
    private String email;
    private String lastConnectedDateTime;
    private String isLocked;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean ADCB;
    private Boolean GDCB;
    private Boolean MDCB;
    private Boolean MSDCB;
    private Boolean NDCB;
    private Boolean PDCB;
    private Boolean SDCB;
}
