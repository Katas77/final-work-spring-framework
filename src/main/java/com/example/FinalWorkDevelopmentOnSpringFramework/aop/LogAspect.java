
package com.example.FinalWorkDevelopmentOnSpringFramework.aop;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LogAspect {
    @Before("@annotation(com.example.annotations.Trackable)")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String message = String.format("Метод %s вызван с аргументами: %s", methodName, Arrays.toString(args));
        log.info(message);
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object proceed = joinPoint.proceed();
        long executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        log.info("Время выполнения метода {} составило {} мс.", joinPoint.getSignature().getName(), executionTime);
        return proceed;
    }


}














