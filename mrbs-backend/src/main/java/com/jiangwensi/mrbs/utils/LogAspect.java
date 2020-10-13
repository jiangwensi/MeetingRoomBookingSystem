package com.jiangwensi.mrbs.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by Jiang Wensi on 6/9/2020
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
//com\jiangwensi\mrbs\controller\RoomController.java

    @Before("execution(* com.jiangwensi.mrbs.controller.*.*(..))")
    public void logEnterMethod(JoinPoint joinPoint) {
        Class myClass = joinPoint.getSignature().getDeclaringType();
        joinPoint.getArgs();
        log.info("Before:" + joinPoint.getSignature().getDeclaringTypeName());
        Object[] signatureArgs = joinPoint.getArgs();
        for (Object signatureArg : signatureArgs) {
            log.info("[" + signatureArg + "]");
        }
    }

    @AfterThrowing(pointcut = "execution(* com.jiangwensi.mrbs.controller.*.*(..))", throwing =
            "ex")
    public void afterThrowing(Exception ex) throws Throwable {
        log.info("AfterThrowing: {}", ex);
    }
}
