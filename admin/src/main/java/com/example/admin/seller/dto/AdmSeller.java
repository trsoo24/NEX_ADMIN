package com.example.admin.seller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdmSeller {
    private String sellerName;
    private String sellerContact;
    private String stdDt;
    private String blockYn;
    private Date regDt;
    private Date updDt;
    private String updId;
    private Date blockDt;
    private String blockId;
}
