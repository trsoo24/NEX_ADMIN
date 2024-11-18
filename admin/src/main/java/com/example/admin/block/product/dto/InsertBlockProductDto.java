package com.example.admin.block.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBlockProductDto {
    private String dcb;
    private Long productNo;
    private String product;
    private String mbrId;
}
