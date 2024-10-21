package com.example.admin.repository.mapper.schedule_manage;

import com.example.admin.domain.dto.schedule_manage.ModifyScheduleManageRequest;
import com.example.admin.domain.dto.schedule_manage.GetScheduleManageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleManageMapper {
    List<GetScheduleManageResponse> getSchedulerStatus();
    void updateSchedulerStatus(ModifyScheduleManageRequest request);
}
