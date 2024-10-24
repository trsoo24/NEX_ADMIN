package com.example.admin.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DCBCategory {
    private int memberId;
    private String ADCB;
    private String GDCB;
    private String MDCB;
    private String MSDCB;
    private String NDCB;
    private String PDCB;
    private String SDCB;
}
