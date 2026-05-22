package com.Tecsup.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ErrorAspect {

    @AfterThrowing(pointcut = "execution(* com.Tecsup.service..*(..))", throwing = "exception")
    public void registrarError(JoinPoint joinPoint, Throwable exception) {
        log.error("Error en metodo {}: {}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }
}
