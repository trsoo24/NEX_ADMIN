package com.example.admin.domain.dto.schedule_manage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyScheduleManageRequest {
    private String scheduler;
    private String server1;
    private String server2;
    private String modifier;
}
