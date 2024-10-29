package com.example.admin.type_limit.mapper;

import com.example.admin.type_limit.dto.GetTypeLimitDto;
import com.example.admin.type_limit.dto.UpdateTypeLimitDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TypeLimitMapper {
    List<GetTypeLimitDto> selectTypeLimitList();
    void updateTypeLimit(UpdateTypeLimitDto updateTypeLimitDto);
}
