package com.example.admin.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockSellerDto {
    private String updId;
    private String blockId;
    private List<String> sellerNames;
}
