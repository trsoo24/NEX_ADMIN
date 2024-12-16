package com.example.admin.block.ctn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBlockCtnDto {
    private String dcb;
    private String ctn;
    private String mbrId;

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
}
