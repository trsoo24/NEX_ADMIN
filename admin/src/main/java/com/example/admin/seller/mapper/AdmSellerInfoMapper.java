package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.InsertSellerInfo;
import com.example.admin.seller.dto.AdmSeller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdmSellerInfoMapper {
    void insertAdmSeller(InsertSellerInfo insertSellerInfo);
    boolean existSeller(String sellerName);
    AdmSeller selectSeller(String sellerName);
    List<AdmSeller> selectAllSeller(Map<String, String> map);
    List<AdmSeller> searchSellers(Map<String, String> map);
    void blockSeller(Map<String, String> map);
    int countSeller();
}
