package com.example.admin.type_limit.service;

import com.example.admin.type_limit.dto.GetTypeLimitDto;
import com.example.admin.type_limit.dto.UpdateTypeLimitDto;
import com.example.admin.type_limit.mapper.TypeLimitMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeLimitService {
    private final TypeLimitMapper typeLimitMapper;

    public List<GetTypeLimitDto> getTypeLimitDtoList() {
        return typeLimitMapper.selectTypeLimitList();
    }

    public void updateTypeLimit(UpdateTypeLimitDto updateTypeLimitDto) {
        typeLimitMapper.updateTypeLimit(updateTypeLimitDto);
    }
}
