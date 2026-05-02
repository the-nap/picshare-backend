package com.picshare.userservice.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(* com.picshare.userservice..*)")
    public void loggedMethods() {}


    @Before("loggedMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[{}] Called method: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "loggedMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[{}] Method {} returned: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "loggedMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.warn("[{}] Exception in method {}: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), exception.getMessage());
    }
}
