package com.example.admin.domain.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayDetailDto {
    private String ctn;
    private String purchaseDate;
    private String cancelDate;
    private Double price;
    private String paymentType; // Enum OR Properties
    private String productName;
    private String company;
    private String merchantName;
}
