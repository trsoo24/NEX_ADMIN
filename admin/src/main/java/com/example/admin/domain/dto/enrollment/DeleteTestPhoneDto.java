package com.example.admin.domain.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTestPhoneDto {
    private String dcb;
    private List<String> ctns;
}
