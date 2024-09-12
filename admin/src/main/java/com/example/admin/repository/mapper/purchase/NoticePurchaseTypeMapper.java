package com.example.admin.repository.mapper.purchase;

import com.example.admin.domain.entity.purchase.NoticePurchaseType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseTypeMapper {
    void insertNotiPurchaseType(NoticePurchaseType noticePurchaseType);
}
