package com.example.admin.repository.mapper.payment;

import com.example.admin.domain.dto.payment.PayDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayDetailMapper {
    List<PayDetailDto> getPayDetailsByProductName(Map<String, Object> map);
    List<PayDetailDto> getPayDetailsByCtn(Map<String, Object> map);
    List<PayDetailDto> getPayDetailsByCompany(Map<String, Object> map);

}
