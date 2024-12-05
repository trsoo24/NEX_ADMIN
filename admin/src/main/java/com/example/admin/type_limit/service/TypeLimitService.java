package com.example.admin.type_limit.service;

import com.example.admin.type_limit.dto.GetTypeLimitDto;
import com.example.admin.type_limit.dto.UpdateTypeLimitDto;
import com.example.admin.type_limit.mapper.TypeLimitMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeLimitService {
    private final TypeLimitMapper typeLimitMapper;

    public List<GetTypeLimitDto> getTypeLimitDtoList() {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 한도 전체 조회 API", trxNo);

        List<GetTypeLimitDto> typeLimitDtoList = typeLimitMapper.selectTypeLimitList();

        log.info("[{}] 응답 = 한도 전체 조회 완료", trxNo);

        return sortList(typeLimitDtoList);
    }

    @Transactional
    public void updateTypeLimit(List<UpdateTypeLimitDto> updateTypeLimitDto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 한도 금액 변경 요청", trxNo);

        for (UpdateTypeLimitDto dto : updateTypeLimitDto) {
            typeLimitMapper.updateTypeLimit1(dto);
            typeLimitMapper.updateTypeLimit2(dto);
        }

        log.info("[{}] 응답 = 한도 금액 변경 완료", trxNo);
    }

    private List<GetTypeLimitDto> sortList(List<GetTypeLimitDto> list) {
        return list.stream()
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.getElapseMonth()))) // GetTypeLimitDto 내 elapseMonth 값 0, 1, 2, 3, 6, 12 순으로 정렬하기 위해 Integer 로 변환 후 비교하여 정렬
                .collect(Collectors.toList());
    }
}
