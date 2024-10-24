package com.example.admin.scheduler.mapper;

import com.example.admin.scheduler.dto.ModifyScheduleManageRequest;
import com.example.admin.scheduler.dto.GetScheduleManageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleManageMapper {
    List<GetScheduleManageResponse> getSchedulerStatus();
    void updateSchedulerStatus(ModifyScheduleManageRequest request);
}
