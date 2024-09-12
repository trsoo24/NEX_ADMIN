package com.example.admin.repository.mapper.purchase;

import com.example.admin.domain.entity.purchase.NoticePurchaseProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseProductMapper {
    void insertNotiPurchaseProduct(NoticePurchaseProduct noticePurchaseProduct);
}
