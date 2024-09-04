package com.example.admin.domain.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBlockItemDto {
    private String dcb;
    private String item;
    private String mbrId;
}
