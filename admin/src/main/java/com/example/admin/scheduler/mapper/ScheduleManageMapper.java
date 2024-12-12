package com.example.admin.scheduler.mapper;

import com.example.admin.scheduler.dto.ModifyScheduleManageRequest;
import com.example.admin.scheduler.dto.ScheduleManageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleManageMapper {
    List<ScheduleManageDto> getSchedulerStatus();
    boolean updateSchedulerStatus(ModifyScheduleManageRequest request);
}
