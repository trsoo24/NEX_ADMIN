package com.example.admin.domain.entity.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockCtn {
    private int blockId;
    private String ctn;
    private String regDt;
    private String mbrId;
}
