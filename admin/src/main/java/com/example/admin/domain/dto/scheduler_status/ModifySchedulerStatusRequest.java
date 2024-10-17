package com.example.admin.domain.dto.scheduler_status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifySchedulerStatusRequest {
    private String scheduler;
    private String server1;
    private String server2;
    private String modifier;
}
