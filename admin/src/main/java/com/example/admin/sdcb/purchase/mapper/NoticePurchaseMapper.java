package com.example.admin.sdcb.purchase.mapper;

import com.example.admin.sdcb.purchase.dto.NoticePurchase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseMapper {
    void insertNotiPurchase(NoticePurchase noticePurchase);
}
