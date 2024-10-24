package com.example.admin.voc.mapper;

import com.example.admin.voc.dto.InsertVocClassification;
import com.example.admin.voc.dto.ProvisionInfoDto;
import com.example.admin.voc.dto.SmsInfoDto;
import com.example.admin.voc.dto.UpdateVocHistoryDto;
import com.example.admin.voc.dto.ProvisioningInfo;
import com.example.admin.voc.dto.SmsInfo;
import com.example.admin.voc.dto.VocClassification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VocMapper {
    void insertProvisioningInfo(ProvisioningInfo provisioningInfo);
    void insertSmsInfo(SmsInfo smsInfo);
    void insertVocHistory(InsertVocClassification vocClassification);
    void updateVocHistory(UpdateVocHistoryDto updateVocHistoryDto);
    List<SmsInfoDto> selectSmsmoListByCtn(Map<String, Object> map);
    List<ProvisionInfoDto> selectProvisioningListByCtn(Map<String, Object> map);
    List<VocClassification> selectVocHistory(Map<String, Object> map);
}
