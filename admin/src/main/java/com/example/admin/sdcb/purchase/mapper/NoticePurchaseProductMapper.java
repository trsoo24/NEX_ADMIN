package com.example.admin.sdcb.purchase.mapper;

import com.example.admin.sdcb.purchase.dto.NoticePurchaseProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseProductMapper {
    void insertNotiPurchaseProduct(NoticePurchaseProduct noticePurchaseProduct);
}
