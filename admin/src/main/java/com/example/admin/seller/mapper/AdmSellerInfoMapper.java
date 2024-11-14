package com.example.admin.seller.mapper;

import com.example.admin.seller.dto.InsertSellerInfo;
import com.example.admin.seller.dto.AdmSeller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdmSellerInfoMapper {
    List<AdmSeller> searchSellers(Map<String, String> map);
    void blockSeller(Map<String, Object> map);
}
