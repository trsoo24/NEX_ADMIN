package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
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
    public StatusResult insertAdminItem(@RequestBody @Valid AdminItemSaleDto dto) {
        adminItemSalesService.createAdminItem(dto);

        return new StatusResult(true);
    }

    @GetMapping()
    public PageResult<AdminItemSales> getAllItems(@RequestParam("page") @Valid int page, @RequestParam("pageSize") @Valid int pageSize, @RequestParam("dcb") @Valid String dcb) {
        Page<AdminItemSales> adminItemSales =  adminItemSalesService.getAllAdminItemSales(page, pageSize, dcb);

        return new PageResult<>(true, adminItemSales);
    }
}
