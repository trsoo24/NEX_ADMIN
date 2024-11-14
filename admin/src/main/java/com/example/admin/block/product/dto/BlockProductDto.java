package com.example.admin.block.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockProductDto {
    private Long productNo; // 상품 번호
    private String productName; // 상품명
    private String regDt;
    private String regId;
    private final String dcb = "GDCB";
}
