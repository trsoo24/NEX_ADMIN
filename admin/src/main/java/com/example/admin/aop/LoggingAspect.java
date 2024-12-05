package com.example.admin.aop;

import com.example.admin.common.service.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController(){}


    @Before("restController()")
    public void beforeAPI(JoinPoint joinPoint)  {
        String apiName = joinPoint.getSignature().getName();
        String trxNo = LogUtils.generateTrxNo();

        MDC.put("trxNo", trxNo);

        // MDC: 스레드-세이프한 로깅 컨텍스트
        log.info("[{}] ========== {} START ==========", trxNo, apiName);
    }


    @After("restController()")
    public void AfterAPI(JoinPoint joinPoint) {
        String apiName = joinPoint.getSignature().getName();
        String trxNo = MDC.get("trxNo");

        log.info("[{}] ========== {} END ==========", trxNo, apiName);
        log.info("");

        MDC.remove("trxNo");
    }
}
