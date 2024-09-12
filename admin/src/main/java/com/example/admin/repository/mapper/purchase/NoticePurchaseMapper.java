package com.example.admin.repository.mapper.purchase;

import com.example.admin.domain.entity.purchase.NoticePurchase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseMapper {
    void insertNotiPurchase(NoticePurchase noticePurchase);
}
