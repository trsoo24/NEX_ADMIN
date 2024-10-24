package com.example.admin.testphone.mapper;

import com.example.admin.testphone.dto.TestPhone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestPhoneMapper {
    void insertTestPhone(TestPhone testPhone);

    boolean existsCtn(String ctn);

    List<TestPhone> getAllTestPhone(Map<String, Object> map);

    int countCtn();

    void deleteCtn(String ctn);
}
