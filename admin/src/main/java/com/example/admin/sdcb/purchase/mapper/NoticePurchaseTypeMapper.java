package com.example.admin.sdcb.purchase.mapper;

import com.example.admin.sdcb.purchase.dto.NoticePurchaseType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticePurchaseTypeMapper {
    void insertNotiPurchaseType(NoticePurchaseType noticePurchaseType);
}
