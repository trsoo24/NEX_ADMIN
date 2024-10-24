package com.example.admin.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0); // 0으로 설정 시 유휴 스레드가 비활성화
        executor.setMaxPoolSize(10); // 최대 스레드풀 수
        executor.setQueueCapacity(50); // 큐에 대기 중인 작업의 수
        executor.setThreadNamePrefix("ASYNC-");
        executor.setKeepAliveSeconds((int) TimeUnit.MINUTES.toSeconds(1)); // core pool size를 초과하는 스레드는 유휴 상태 시 종료된다.
        executor.initialize();
        return executor;
    }

}

