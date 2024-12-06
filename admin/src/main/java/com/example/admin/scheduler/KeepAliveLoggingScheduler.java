package com.example.admin.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeepAliveLoggingScheduler {

    @Scheduled(fixedRate = 60000)
    public void logKeepAlive() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>> Keep Alive <<<<<<<<<<<<<<<<<<<<<<<<<");
    }

}
