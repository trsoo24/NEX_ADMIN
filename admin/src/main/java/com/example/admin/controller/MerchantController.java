package com.example.admin.controller;

import com.example.admin.domain.dto.merchant.BlockMerchantDto;
import com.example.admin.domain.dto.merchant.InsertMerchantInfo;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import com.example.admin.service.merchant.AdmMerchantService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
        return admMerchantService.searchMerchantToPage(dcb, merchantNm, startDate, endDate, page, pageSize);
    }

    @GetMapping("/excel")
    public void exportMerchantListExcel(@RequestParam("dcb") @Valid String dcb,
                                        @RequestParam("merchantNm") String merchantNm,
                                        @RequestParam("startDate") @Valid String startDate,
                                        @RequestParam("endDate") @Valid String endDate,
                                        HttpServletResponse response) throws IllegalAccessException, IOException {
        admMerchantService.exportMerchantListExcel(dcb, merchantNm, startDate, endDate, response);
    }

    @PutMapping("/block")
    public void blockMerchant(@RequestBody @Valid BlockMerchantDto dto) {
        admMerchantService.blockMerchant(dto);
    }
}
