package com.example.admin.scheduler.service;

import com.example.admin.scheduler.dto.ModifyScheduleManageRequest;
import com.example.admin.scheduler.dto.GetScheduleManageResponse;
import com.example.admin.scheduler.mapper.ScheduleManageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {

    private final ScheduleManageMapper scheduleManageMapper;


    // 스케줄러 기동 현황을 조회한다.
    public List<GetScheduleManageResponse> getSchedulerStatus() {
        String trxNo = MDC.get("trxNo");

        List<GetScheduleManageResponse> scheduleManageResponseList = scheduleManageMapper.getSchedulerStatus();

        log.info("[{}] ADMIN 스케줄러 {} 건 호출", trxNo, scheduleManageResponseList.size());

        return scheduleManageResponseList;
    }


    // 스케줄러의 기동 서버를 변경한다.
    public void updateSchedulerStatus(ModifyScheduleManageRequest request) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 스케줄러 서버 변경 요청", trxNo);
        boolean updateResponse = scheduleManageMapper.updateSchedulerStatus(request);

        if (updateResponse) {
            String server1 = "SERVER1";
            String server2 = "SERVER2";
            if ("Y".equals(request.getServer1())) {
                log.info("[{}] ADMIN {} 스케줄러 기동 서버 {} -> {} 수정 완료", trxNo, request.getScheduler(), server2, server1);
            } else {
                log.info("[{}] ADMIN {} 스케줄러 기동 서버 {} -> {} 수정 완료", trxNo, request.getScheduler(), server1, server2);
            }
        } else {
            log.info("[{}] ADMIN {} 스케줄러 기동 서버 수정 실패", trxNo, request.getScheduler());
        }
    }

}
