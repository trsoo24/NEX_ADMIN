package com.example.admin.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequestDto {
    private Integer memberId;
    private String password;
    private String email;
    private List<String> services;
}
