package com.example.admin.domain.dto.voc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvisionInfoDto {
    private Integer subNo;
    private String operatorUserToken;
    private String isProvisioned;
    private String updateDt;
    private String createDt;
}
