package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.InsertMerchantInfo;
import com.example.admin.seller.dto.AdmMerchant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdmMerchantInfoMapper {
    void insertAdmMerchant(InsertMerchantInfo insertMerchantInfo);
    boolean existMerchant(String merchantNm);
    AdmMerchant selectMerchant(String merchantNm);
    List<AdmMerchant> selectAllMerchant(Map<String, String> map);
    List<AdmMerchant> searchMerchants(Map<String, String> map);
    void blockMerchant(Map<String, String> map);
    int countMerchant();
}
