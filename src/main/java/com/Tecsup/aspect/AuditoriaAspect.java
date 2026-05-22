package com.Tecsup.aspect;

import com.Tecsup.service.AuditoriaLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private final AuditoriaLogService auditoriaLogService;
    private final HttpServletRequest request;

    @AfterReturning(
            "within(com.Tecsup.controller..*)"
                    + " && !within(com.Tecsup.controller.AuditoriaLogController)"
                    + " && (@annotation(org.springframework.web.bind.annotation.PostMapping)"
                    + " || @annotation(org.springframework.web.bind.annotation.PutMapping)"
                    + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping))"
    )
    public void registrarAuditoria(JoinPoint joinPoint) {
        String metodo = joinPoint.getSignature().toShortString();
        String accion = request.getMethod() + " " + request.getRequestURI();
        String usuario = obtenerUsuarioAutenticado();
        auditoriaLogService.registrar(accion, metodo, usuario);
    }

    private String obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return "anonimo";
        }
        return authentication.getName();
    }
}
