package com.picshare.feed_service.aspect;

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
  
  @Pointcut("execution(* com.picshare.feed_service.controller.*.*(..))")
  public void controllerMethods() {}


  @Before("controllerMethods()")
  public void logBefore(JoinPoint joinPoint) {
    log.info("Called controller method: {}", joinPoint.getSignature().getName());
    log.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
  }

  @AfterReturning(pointcut = "controllerMethods()", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    log.info("Controller method {} returned: {}", joinPoint.getSignature().getName(), result);
  }
  
  @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
  public void logException(JoinPoint joinPoint, Throwable exception) {
    log.warn("Exception in method {}: {}", joinPoint.getSignature().getName(), exception.getMessage());
  }

}
