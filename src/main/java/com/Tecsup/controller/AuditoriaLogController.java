package com.Tecsup.controller;

import com.Tecsup.dto.AuditoriaLogResponseDTO;
import com.Tecsup.service.AuditoriaLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaLogController {

    private final AuditoriaLogService auditoriaLogService;

    @GetMapping
    public ResponseEntity<List<AuditoriaLogResponseDTO>> listar() {
        return ResponseEntity.ok(auditoriaLogService.listar());
    }
}
