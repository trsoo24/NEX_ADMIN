package com.example.admin.domain.dto.member.type;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("USER"),ROLE_MANAGER("MANAGER"), ROLE_ADMIN("SUPER_ADMIN");

    private final String role;

    Role(String role) {this.role = role;}
}
