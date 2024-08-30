package com.example.admin.domain.entity.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_SUPER("SUPER");

    private final String type;
}
