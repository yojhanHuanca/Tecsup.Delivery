package com.Tecsup.service;

import com.Tecsup.dto.AuditoriaLogResponseDTO;
import com.Tecsup.model.AuditoriaLog;
import com.Tecsup.repository.AuditoriaLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaLogService {

    private final AuditoriaLogRepository auditoriaLogRepository;

    @Transactional(readOnly = true)
    public List<AuditoriaLogResponseDTO> listar() {
        return auditoriaLogRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void registrar(String accion, String metodo, String usuario) {
        AuditoriaLog log = AuditoriaLog.builder()
                .accion(accion)
                .metodo(metodo)
                .fecha(LocalDateTime.now())
                .usuario(usuario)
                .build();
        auditoriaLogRepository.save(log);
    }

    private AuditoriaLogResponseDTO toResponse(AuditoriaLog log) {
        return new AuditoriaLogResponseDTO(
                log.getId(),
                log.getAccion(),
                log.getMetodo(),
                log.getFecha(),
                log.getUsuario()
        );
    }
}
