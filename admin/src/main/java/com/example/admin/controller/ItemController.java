package com.example.admin.controller;

import com.example.admin.domain.dto.item.AdminItemSaleDto;
import com.example.admin.domain.entity.item.AdminItemSales;
import com.example.admin.service.item.AdminItemSalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final AdminItemSalesService adminItemSalesService;


    @PostMapping()
    public void insertAdminItem(@RequestBody @Valid AdminItemSaleDto dto) {
        adminItemSalesService.createAdminItem(dto);
    }

    @GetMapping()
    public Page<AdminItemSales> getAllItems(@RequestParam("page") @Valid int page, @RequestParam("pageSize") @Valid int pageSize, @RequestParam("dcb") @Valid String dcb) {
        return adminItemSalesService.getAllAdminItemSales(page, pageSize, dcb);
    }
}
