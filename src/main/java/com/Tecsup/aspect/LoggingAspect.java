package com.Tecsup.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.Tecsup.service..*(..))")
    public Object registrarInicioYFin(ProceedingJoinPoint joinPoint) throws Throwable {
        String metodo = joinPoint.getSignature().toShortString();
        log.info("Inicio de ejecucion: {}", metodo);
        Object resultado = joinPoint.proceed();
        log.info("Fin de ejecucion: {}", metodo);
        return resultado;
    }
}
