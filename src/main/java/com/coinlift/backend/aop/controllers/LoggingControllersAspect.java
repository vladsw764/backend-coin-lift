package com.coinlift.backend.aop.controllers;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LoggingControllersAspect {

    @Pointcut("execution(public * com.coinlift.backend.controllers.*.*(..))")
    public void allPublicMethods() {
    }

    @Before("allPublicMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("\u001B[32m" + "Controller: " + methodName + " - start." + "\u001B[0m");
    }

    @After("allPublicMethods()")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("\u001B[32m" + "Controller: " + methodName + " - end." + "\u001B[0m");
    }
}
