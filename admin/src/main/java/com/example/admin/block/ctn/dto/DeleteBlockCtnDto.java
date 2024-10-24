package com.example.admin.block.ctn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlockCtnDto {
    private String dcb;
    private List<String> ctns;
}
