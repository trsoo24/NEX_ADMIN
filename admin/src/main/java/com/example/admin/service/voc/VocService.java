package com.example.admin.service.voc;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.voc.InsertVocClassification;
import com.example.admin.domain.dto.voc.ProvisionInfoDto;
import com.example.admin.domain.dto.voc.SmsInfoDto;
import com.example.admin.domain.dto.voc.UpdateVocHistoryDto;
import com.example.admin.domain.entity.gdcb.ProvisioningInfo;
import com.example.admin.domain.entity.gdcb.SmsInfo;
import com.example.admin.domain.entity.voc.VocClassification;
import com.example.admin.repository.mapper.voc.VocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public void insertVocClassification(InsertVocClassification insertVocClassification) {
        // CTN 값 내 "-" 제거 및 01012345678 형식으로 변경
        insertVocClassification.setCtn(functionUtil.transCtn(insertVocClassification.getCtn()));

        vocMapper.insertVocHistory(insertVocClassification);
    }

    public void updateVocClassification(UpdateVocHistoryDto updateVocHistoryDto) {
        vocMapper.updateVocHistory(updateVocHistoryDto);
    }

    public Page<VocClassification> getVocClassificationList(String dcb, String ctn, int page, int pageSize) {
        return functionUtil.toPage(getVocClassificationList(dcb, ctn), page, pageSize);
    }

    private List<VocClassification> getVocClassificationList(String dcb, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("ctn", functionUtil.transCtn(ctn));

        return vocMapper.selectVocHistory(requestMap);
    }
}
