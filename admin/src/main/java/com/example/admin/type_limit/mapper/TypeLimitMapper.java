package com.example.admin.type_limit.mapper;

import com.example.admin.type_limit.dto.GetTypeLimitDto;
import com.example.admin.type_limit.dto.UpdateTypeLimitDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TypeLimitMapper {
    List<GetTypeLimitDto> selectTypeLimitList();
    void updateTypeLimit1(UpdateTypeLimitDto updateTypeLimitDto);
    void updateTypeLimit2(UpdateTypeLimitDto updateTypeLimitDto);
}
