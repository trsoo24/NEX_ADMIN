package com.example.admin.repository.mapper.payment;

import com.example.admin.domain.dto.payment.PayDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayDetailMapper {
    List<PayDetailDto> selectPaymentDetailByProductName(Map<String, Object> map);
    List<PayDetailDto> selectPaymentDetailByCtn(Map<String, Object> map);
    List<PayDetailDto> selectPaymentDetailByCompanyName(Map<String, Object> map);
}
