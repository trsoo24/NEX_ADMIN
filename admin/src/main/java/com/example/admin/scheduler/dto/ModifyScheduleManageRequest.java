package com.example.admin.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyScheduleManageRequest {
    private String scheduleServer;
    private String modifier;
    private String scheduleCd;
}
