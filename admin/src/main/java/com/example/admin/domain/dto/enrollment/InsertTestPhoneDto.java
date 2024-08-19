package com.example.admin.domain.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertTestPhoneDto {
    private String ctn;
    private String name;
    private String email;
}
