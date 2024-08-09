package com.example.admin.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String position;
    private String memberName;
    private String password;
    private String name;
    private String ctn;
    private String email;
}
