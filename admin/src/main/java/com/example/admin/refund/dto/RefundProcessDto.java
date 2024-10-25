package com.example.admin.refund.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefundProcessDto {
    private String correlationId;
    private String ctn;
}
