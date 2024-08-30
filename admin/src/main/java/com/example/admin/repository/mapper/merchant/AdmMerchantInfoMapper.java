package com.example.admin.repository.mapper.merchant;

import com.example.admin.domain.dto.merchant.InsertMerchantInfo;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdmMerchantInfoMapper {
    void insertAdmMerchant(InsertMerchantInfo insertMerchantInfo);
    boolean existMerchant(String merchantName);
    List<AdmMerchant> selectAllMerchant(Map<String, String> map);
    List<AdmMerchant> searchMerchants(Map<String, String> map);

    int countMerchant();
}
