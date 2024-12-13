package com.example.admin.block.fee_type.service;

import com.example.admin.block.fee_type.dto.BlockFeeTypeDto;
import com.example.admin.block.fee_type.dto.DeleteFeeTypeDto;
import com.example.admin.block.fee_type.dto.InsertBlockFeeTypeDto;
import com.example.admin.block.fee_type.mapper.BlockFeeTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockFeeTypeService {
    private final BlockFeeTypeMapper blockFeeTypeMapper;

    public void insertBlockFeeType(InsertBlockFeeTypeDto blockFeeTypeDto) {
        String trxNo = MDC.get("trxNo");

        blockFeeTypeMapper.insertBlockFeeType(blockFeeTypeDto);

        log.info("[{}] 응답 = {} 요금제 차단 완료", trxNo, blockFeeTypeDto.getFeeTypeName());
    }

    public List<BlockFeeTypeDto> getAllBlockFeeType() {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 차단 요금제 전체 조회", trxNo);

        List<BlockFeeTypeDto> feeTypeDtoList = blockFeeTypeMapper.getAllBlockFeeType();

        log.info("[{}] 응답 = 차단 요금제 {} 건 조회 완료", trxNo, feeTypeDtoList.size());

        return feeTypeDtoList;
    }

    public void deleteBlockFeeType(DeleteFeeTypeDto dto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 차단 요금제 해제 API", trxNo);

        List<String> feeTypeCode = dto.getFeeTypeCodes();

        boolean deleteResponse = blockFeeTypeMapper.deleteBlockFeeType(feeTypeCode);

        if (deleteResponse) {
            log.info("[{}] 응답 = 차단 요금제 {} 건 차단 해제 완료", trxNo, dto.getFeeTypeCodes().size());
        } else {
            log.info("[{}] 응답 = 차단 요금제 차단 해제 실패", trxNo);
        }
    }

    public boolean existFeeType(String feeTypeCode) {
        String trxNo = MDC.get("trxNo");

        boolean result = blockFeeTypeMapper.existsFeeType(feeTypeCode);

        if (result) {
            log.info("[{}] 응답 = {} 는 이미 DB 에 존재하는 차단 요금제입니다. ", trxNo, feeTypeCode);
        } else {
            log.info("[{}] DB 내 중복 데이터 없음 ", trxNo);
        }
        return result;
    }
}
