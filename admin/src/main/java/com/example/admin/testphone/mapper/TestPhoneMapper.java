package com.example.admin.testphone.mapper;

import com.example.admin.testphone.dto.DeleteTestPhoneDto;
import com.example.admin.testphone.dto.InsertTestPhoneDto;
import com.example.admin.testphone.dto.TestPhone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestPhoneMapper {
    void insertTestPhone(InsertTestPhoneDto dto);

    boolean existsCtn(String ctn);

    List<TestPhone> getAllTestPhone();

    void deleteCtn(String ctn);
}
