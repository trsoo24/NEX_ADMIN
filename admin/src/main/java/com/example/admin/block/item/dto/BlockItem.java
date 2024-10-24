package com.example.admin.block.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockItem {
    private Long itemNo; // 상품 번호
    private String itemNm; // 상품명
    private String regDt;
    private String regId;
    private String dcb;
}
