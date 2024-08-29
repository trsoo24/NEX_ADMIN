package com.example.admin.domain.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBlockCtnDto {
    private String ctn;
    private String mbrId;
    private String dcb;
}
