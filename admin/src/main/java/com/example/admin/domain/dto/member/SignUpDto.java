package com.example.admin.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String team;
    private String role;
    private List<String> services;
    private String username;
    private String password;
    private String name;
    private String email;
}
