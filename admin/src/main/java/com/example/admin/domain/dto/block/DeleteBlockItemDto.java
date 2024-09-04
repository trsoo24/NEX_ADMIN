package com.example.admin.domain.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlockItemDto {
    private String dcb;
    private List<Long> items;
}
