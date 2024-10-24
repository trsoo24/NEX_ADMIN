package com.example.admin.voc.mapper;

import com.example.admin.voc.dto.InsertVocDivision;
import com.example.admin.voc.dto.ProvisionInfoDto;
import com.example.admin.voc.dto.SmsInfoDto;
import com.example.admin.voc.dto.UpdateVocHistoryDto;
import com.example.admin.voc.dto.ProvisioningInfo;
import com.example.admin.voc.dto.SmsInfo;
import com.example.admin.voc.dto.VocDivision;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VocMapper {
    void insertProvisioningInfo(ProvisioningInfo provisioningInfo);
    void insertSmsInfo(SmsInfo smsInfo);
    void insertVocHistory(InsertVocDivision vocClassification);
    void updateVocHistory(UpdateVocHistoryDto updateVocHistoryDto);
    void deleteVocHistory(Integer id);
    List<SmsInfoDto> selectSmsmoListByCtn(Map<String, Object> map);
    List<ProvisionInfoDto> selectProvisioningListByCtn(Map<String, Object> map);
    List<VocDivision> selectVocHistory(Map<String, Object> map);
}
