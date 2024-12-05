package com.example.admin.block.product.service;

import com.example.admin.block.product.dto.DeleteBlockProductDto;
import com.example.admin.block.product.dto.InsertBlockProductDto;
import com.example.admin.block.product.dto.BlockProductDto;
import com.example.admin.block.product.mapper.BlockProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockProductService {
    private final BlockProductMapper blockProductMapper;

    public void insertBlockProduct(InsertBlockProductDto dto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 상품 차단 요청", trxNo);

        boolean insertResponse = blockProductMapper.insertBlockProduct(dto);

        if (insertResponse) {
            log.info("[{}] 응답 = 상품 {} 차단 완료", trxNo, dto.getProduct());
        } else {
            log.info("[{}] 응답 = 상품 {} 차단 실패", trxNo, dto.getProduct());
        }
    }

    public List<BlockProductDto> getBlockProductList(String product) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 차단 상품 조회", trxNo);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);

        List<BlockProductDto> blockProductDtoList = blockProductMapper.getBlockProductList(map);

        log.info("[{}] 응답 = 차단 상품 {} 건 조회 완료", trxNo, blockProductDtoList.size());

        return blockProductDtoList;
    }

    public void deleteBlockProduct(DeleteBlockProductDto dto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 차단 상품 해제 요청", trxNo);

        boolean deleteResponse = blockProductMapper.deleteBlockProduct(dto.getProducts());

        if (deleteResponse) {
            log.info("[{}] 응답 = 차단 상품 {} 건 해제 완료", trxNo, dto.getProducts());
        } else {
            log.info("[{}] 응답 = 차단 상품 해제 실패", trxNo);
        }
    }
}
