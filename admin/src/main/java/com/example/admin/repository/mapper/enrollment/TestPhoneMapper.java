package com.example.admin.repository.mapper.enrollment;

import com.example.admin.domain.entity.enrollment.TestPhone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestPhoneMapper {
    void insertTestPhone(TestPhone testPhone);

    boolean existsCtn(String ctn);

    List<TestPhone> getAllTestPhone(Map<String, Integer> map);

    int countCtn();

    void deleteCtn(String ctn);
}
