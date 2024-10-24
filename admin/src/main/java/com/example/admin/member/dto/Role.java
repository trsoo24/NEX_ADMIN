package com.example.admin.member.dto;

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
