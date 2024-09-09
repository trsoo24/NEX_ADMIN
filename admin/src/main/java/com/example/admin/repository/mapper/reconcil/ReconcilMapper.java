package com.example.admin.repository.mapper.reconcil;

import com.example.admin.domain.dto.reconcil.InsertReconcilDto;
import com.example.admin.domain.entity.reconcil.Reconcil;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface ReconcilMapper {
    void insertReconcil(InsertReconcilDto insertReconcilDto);
    List<Reconcil> getReconcilList(Map<String, String> map);
}
