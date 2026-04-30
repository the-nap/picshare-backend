package com.picshare.post_service.aspect;

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
public class ServiceLogging {
  
  @Pointcut("execution(* com.picshare.post_service.service.service..*(..))")
  public void serviceMethods() {}


  @Before("serviceMethods()")
  public void logBefore(JoinPoint joinPoint) {
    log.info("Called service method: {}", joinPoint.getSignature().getName());
    log.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
  }

  @AfterReturning(pointcut = "serviceMethods()", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    log.info("Event method {} returned: {}", joinPoint.getSignature().getName(), result);
  }
  
  @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
  public void logException(JoinPoint joinPoint, Throwable exception) {
    log.warn("Exception in method {}: {}", joinPoint.getSignature().getName(), exception.getMessage());
  }

}

