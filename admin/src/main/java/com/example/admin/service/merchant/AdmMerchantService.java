package com.example.admin.service.merchant;

import com.example.admin.domain.dto.merchant.InsertMerchantInfo;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import com.example.admin.repository.mapper.merchant.AdmMerchantInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdmMerchantService {
    private final AdmMerchantInfoMapper admMerchantInfoMapper;

    public void insertMerchant(InsertMerchantInfo insertMerchantInfo) {
        if (!existMerchant(insertMerchantInfo.getMerchantNm())) {
            admMerchantInfoMapper.insertAdmMerchant(insertMerchantInfo);
        }
    }

    private boolean existMerchant(String merchantNm) {
        return admMerchantInfoMapper.existMerchant(merchantNm);
    }

    public List<AdmMerchant> searchMerchantsWithName(String merchantNm, String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("merchantNm", merchantNm);

        return admMerchantInfoMapper.searchMerchants(map);
    }

    public List<AdmMerchant> searchAllMerchant(String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return admMerchantInfoMapper.selectAllMerchant(map);
    }

    public Page<AdmMerchant> searchMerchantsWithName(String dcb, String merchantNm, String startDate, String endDate, int page, int pageSize) {
        List<AdmMerchant> admMerchantList;
        Map<String, String> map = new HashMap<>();

        if (merchantNm.isEmpty()) {
            admMerchantList = searchAllMerchant(startDate, endDate);
        } else {
            admMerchantList = searchMerchantsWithName(merchantNm, startDate, endDate);
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return new PageImpl<>(admMerchantList, pageable, admMerchantInfoMapper.countMerchant());
    }
}
