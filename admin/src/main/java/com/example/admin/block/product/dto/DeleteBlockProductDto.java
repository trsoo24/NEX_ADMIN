package com.example.admin.block.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlockProductDto {
    private String dcb;
    private List<Long> products;
}
