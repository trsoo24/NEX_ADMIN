package com.example.admin.voc.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.voc.dto.*;
import com.example.admin.voc.mapper.VocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void insertVocDivision(InsertVocDivision insertVocDivision) {
        // CTN 값 내 "-" 제거 및 01012345678 형식으로 변경
        insertVocDivision.setCtn(functionUtil.transCtn(insertVocDivision.getCtn()));

        vocMapper.insertVocHistory(insertVocDivision);
    }

    public void updateVocDivision(UpdateVocHistoryDto updateVocHistoryDto) {
        vocMapper.updateVocHistory(updateVocHistoryDto);
    }

    public Page<VocDivision> getVocDivisionList(String dcb, String writer, String startDate, String endDate, int page, int pageSize) {
        return functionUtil.toPage(getVocDivisionList(dcb, writer, startDate, endDate), page, pageSize);
    }

    private List<VocDivision> getVocDivisionList(String dcb, String writer, String startDate, String endDate) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("writer", writer);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        return vocMapper.selectVocHistory(requestMap);
    }

    @Transactional
    public void deleteVocHistory(List<Integer> vocIdList) {
        for (Integer vocId : vocIdList) {
            vocMapper.deleteVocHistory(vocId);
        }
    }
}
