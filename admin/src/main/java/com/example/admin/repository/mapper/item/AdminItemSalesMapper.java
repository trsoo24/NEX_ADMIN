package com.example.admin.repository.mapper.item;

import com.example.admin.domain.entity.item.AdminItemSales;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminItemSalesMapper {
    void insertAdminItemSales(AdminItemSales adminItemSales);

    boolean existsItem(String itemsalesNm);

    List<AdminItemSales> getAllAdminItemSales();
}
