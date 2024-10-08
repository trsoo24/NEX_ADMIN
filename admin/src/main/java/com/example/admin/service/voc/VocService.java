package com.example.admin.service.voc;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.voc.ProvisionInfoDto;
import com.example.admin.domain.dto.voc.SmsInfoDto;
import com.example.admin.domain.entity.gdcb.ProvisioningInfo;
import com.example.admin.domain.entity.gdcb.SmsInfo;
import com.example.admin.repository.mapper.voc.VocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VocService {
    private final VocMapper vocMapper;
    private final FunctionUtil functionUtil;

    public void insertProvisioningInfo(ProvisioningInfo provisioningInfo) {
        vocMapper.insertProvisioningInfo(provisioningInfo);
    }

    public void insertSmsInfo(SmsInfo smsInfo) {
        vocMapper.insertSmsInfo(smsInfo);
    }

    public Map<String, Object> getGdcbConversionHistory(String dcb, String ctn) {
        List<ProvisionInfoDto> provisioningInfoList = getProvisioningInfoList(dcb, ctn);
        List<SmsInfoDto> smsInfoList = getSmsInfoList(dcb, ctn);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("smsmo 이력", smsInfoList);
        responseMap.put("provisioning 이력", provisioningInfoList);

        return responseMap;
    }

    private List<ProvisionInfoDto> getProvisioningInfoList(String dcb, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("ctn", functionUtil.transCtn(ctn));

        return vocMapper.selectProvisioningListByCtn(requestMap);
    }

    private List<SmsInfoDto> getSmsInfoList(String dcb, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("ctn", functionUtil.transCtn(ctn));

        return vocMapper.selectSmsmoListByCtn(requestMap);
    }
}
