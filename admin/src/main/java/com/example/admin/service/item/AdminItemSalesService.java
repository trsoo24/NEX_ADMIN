package com.example.admin.service.item;

import com.example.admin.domain.dto.item.AdminItemSaleDto;
import com.example.admin.domain.entity.item.AdminItemSales;
import com.example.admin.repository.mapper.item.AdminItemSalesMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminItemSalesService {
    private final AdminItemSalesMapper adminItemSalesMapper;
    private final FunctionUtil functionUtil;
    private static final String[] DCB_ARRAY = {"ADCB", "GDCB", "MDCB", "MSDCB", "NDCB", "PDCB", "SDCB"};

    @Transactional
    public void createAdminItem(AdminItemSaleDto dto) {
        String dcb = findDCB(dto.getDcb());

        if (!existItem(dto.getItemsalesNm())) {
            adminItemSalesMapper.insertAdminItemSales(dto.toNewAdminItemSale());
        }
    }

    private boolean existItem(String itemsalesNm) {
        return adminItemSalesMapper.existsItem(itemsalesNm);
    }

    private String findDCB(String dcb) {
        for (String idx : DCB_ARRAY) {
            if (idx.equals(dcb.toUpperCase())) {
                return idx;
            }
        }
        return null;
    }

    public Page<AdminItemSales> getAllAdminItemSales(int page, int pageSize, String dcb) {
        List<AdminItemSales> adminItemSalesList = adminItemSalesMapper.getAllAdminItemSales();

        return functionUtil.toPage(adminItemSalesList, page, pageSize);
    }
}
