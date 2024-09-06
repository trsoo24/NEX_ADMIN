package com.example.admin.domain.entity.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER"),
    ADMIN("MANAGER"),
    SUPER("SUPER_ADMIN");

    private final String type;
}
