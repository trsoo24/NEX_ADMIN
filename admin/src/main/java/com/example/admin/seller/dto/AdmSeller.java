package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdmSeller {
    private String sellerName;
    private String sellerContact;
    private String stdDt;
    private String blockYn;
    private LocalDate regDt;
    private LocalDate updDt;
    private String updId;
    private LocalDate blockDt;
    private String blockId;
}
