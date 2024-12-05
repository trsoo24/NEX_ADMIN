package com.example.admin.voc.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.voc.dto.*;
import com.example.admin.voc.mapper.VocMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocService {
    private final VocMapper vocMapper;
    private final FunctionUtil functionUtil;

    public Map<String, Object> getGdcbConversionHistory(String ctn) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 전환 이력 조회 CTN = {}", trxNo, ctn);

        List<ProvisionInfoDto> provisioningInfoList = getProvisioningInfoList(ctn);
        List<SmsInfoDto> smsInfoList = getSmsInfoList(ctn);

        log.info("[{}] 응답 = provisioning {} 건 조회, smsmo {} 건 조회 완료", trxNo, provisioningInfoList.size(), smsInfoList.size());

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("smsmoContents", smsInfoList);
        responseMap.put("provisioningContents", provisioningInfoList);

        return responseMap;
    }

    private List<ProvisionInfoDto> getProvisioningInfoList(String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("ctn", functionUtil.transCtn(ctn));

        return vocMapper.selectProvisioningListByCtn(requestMap);
    }

    private List<SmsInfoDto> getSmsInfoList(String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("ctn", functionUtil.transCtn(ctn));

        return vocMapper.selectSmsmoListByCtn(requestMap);
    }
}
