package com.example.admin.repository.mapper.scheduler_status;

import com.example.admin.domain.dto.scheduler_status.ModifySchedulerStatusRequest;
import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchedulerStatusMapper {
    List<SchedulerStatusResponse> getSchedulerStatus();
    void updateSchedulerStatus(ModifySchedulerStatusRequest request);
}