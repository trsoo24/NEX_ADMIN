package com.example.admin.repository.mapper.scheduler_status;

import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulerStatusMapper {
    SchedulerStatusResponse getSchedulerStatus();
}
