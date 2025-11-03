package com.example.FinalWorkDevelopmentOnSpringFramework.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Before("@annotation(com.example.annotations.Trackable)")
    public void logMethodCall(JoinPoint joinPoint) {
        String signature = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Метод {} вызван с аргументами: {}", signature, Arrays.toString(args));
    }

    @Around("@annotation(com.example.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTimeMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("Время выполнения метода {} составило {} мс.",
                    joinPoint.getSignature().toShortString(), executionTimeMs);
        }
    }
}









