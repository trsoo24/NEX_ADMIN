package com.example.admin.voc.mapper;

import com.example.admin.voc.dto.ProvisionInfoDto;
import com.example.admin.voc.dto.SmsInfoDto;
import com.example.admin.voc.dto.ProvisioningInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VocMapper {
    List<SmsInfoDto> selectSmsmoListByCtn(Map<String, Object> map);
    List<ProvisionInfoDto> selectProvisioningListByCtn(Map<String, Object> map);
}
