package com.example.admin.repository.mapper.voc;

import com.example.admin.domain.dto.voc.InsertVocClassification;
import com.example.admin.domain.dto.voc.ProvisionInfoDto;
import com.example.admin.domain.dto.voc.SmsInfoDto;
import com.example.admin.domain.dto.voc.UpdateVocHistoryDto;
import com.example.admin.domain.entity.gdcb.ProvisioningInfo;
import com.example.admin.domain.entity.gdcb.SmsInfo;
import com.example.admin.domain.entity.voc.VocClassification;
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
