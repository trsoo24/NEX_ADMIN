package com.example.admin.block.ctn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockCtnDto {
    private String ctn;
    private String regDt;
    private String mbrId;
    private final String dcb = "GDCB";

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
}
