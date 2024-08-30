package com.example.admin.controller;

import com.example.admin.domain.dto.merchant.InsertMerchantInfo;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import com.example.admin.service.merchant.AdmMerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class MerchantController {
    private final AdmMerchantService admMerchantService;


    @PostMapping
    public void insertMerchant(@RequestBody @Valid InsertMerchantInfo dto) {
        admMerchantService.insertMerchant(dto);
    }

    @GetMapping
    public Page<AdmMerchant> searchAllMerchant(@RequestParam("dcb") @Valid String dcb,
                                               @RequestParam("merchantNm") String merchantNm,
                                               @RequestParam("startDate") @Valid String startDate,
                                               @RequestParam("endDate") @Valid String endDate,
                                               @RequestParam("page") @Valid int page,
                                               @RequestParam("pageSize") @Valid int pageSize) {
        return admMerchantService.searchMerchantsWithName(dcb, merchantNm, startDate, endDate, page, pageSize);
    }
}
