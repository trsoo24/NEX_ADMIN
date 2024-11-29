package com.example.admin.voc.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.voc.dto.*;
import com.example.admin.voc.mapper.VocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VocService {
    private final VocMapper vocMapper;
    private final FunctionUtil functionUtil;

    public Map<String, Object> getGdcbConversionHistory(String dcb, String ctn) {
        List<ProvisionInfoDto> provisioningInfoList = getProvisioningInfoList(dcb, ctn);
        List<SmsInfoDto> smsInfoList = getSmsInfoList(dcb, ctn);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("smsmoContents", smsInfoList);
        responseMap.put("provisioningContents", provisioningInfoList);

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
