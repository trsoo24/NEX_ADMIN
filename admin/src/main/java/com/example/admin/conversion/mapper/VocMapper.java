package com.example.admin.conversion.mapper;

import com.example.admin.conversion.dto.ProvisionInfoDto;
import com.example.admin.conversion.dto.SmsInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VocMapper {
    List<SmsInfoDto> selectSmsmoListByCtn(Map<String, Object> map);
    List<ProvisionInfoDto> selectProvisioningListByCtn(Map<String, Object> map);
}
