package com.example.admin.block.ctn.service;

import com.example.admin.block.ctn.dto.BlockCtnDto;
import com.example.admin.block.ctn.dto.DeleteBlockCtnDto;
import com.example.admin.block.ctn.dto.InsertBlockCtnDto;
import com.example.admin.block.ctn.mapper.BlockCtnMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockCtnService {
    private final BlockCtnMapper blockCtnMapper;

    public void insertBlockCtn(InsertBlockCtnDto blockCtnDto) {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 차단 CTN 생성 ", trxNo);

        blockCtnMapper.insertBlockCtn(blockCtnDto);

        log.info("[{}] 응답 = {} CTN 차단 완료", trxNo, blockCtnDto.getCtn());
    }

    public List<BlockCtnDto> getAllBlockCtn() {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 차단 CTN 전체 조회 ", trxNo);

        List<BlockCtnDto> dtoList = blockCtnMapper.getAllBlockCtn();
        log.info("[{}] 응답 = 차단 CTN {} 건 조회 완료", trxNo, dtoList.size());

        return dtoList;
    }

    public void deleteBlockCtn(DeleteBlockCtnDto dto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 차단 CTN {} 건 차단 해제 API", trxNo, dto.getCtns().size());
        List<String> ctns = dto.getCtns();

        boolean deleteResponse = blockCtnMapper.deleteBlockCtn(ctns);

        if(deleteResponse) {
            log.info("[{}] 응답 = 차단 CTN {} 건 차단 해제 완료", trxNo, ctns.size());
        } else {
            log.info("[{}] 응답 = 차단 CTN  차단 해제 실패", trxNo);
        }
    }

    public boolean existBlockCtn(String ctn) {
        String trxNo = MDC.get("trxNo");

        boolean result = blockCtnMapper.existsCtn(ctn);

        if (result) {
            log.info("[{}] 응답 = {} 는 이미 DB 에 존재하는 차단 CTN 입니다.", trxNo, ctn);
        } else {
            log.info("[{}] DB 내 중복 데이터 없음 ", trxNo);
        }
        return result;
    }
}
