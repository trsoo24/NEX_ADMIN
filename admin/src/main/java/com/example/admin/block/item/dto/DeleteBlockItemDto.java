package com.example.admin.block.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlockItemDto {
    private String dcb;
    private List<Long> products;
}
