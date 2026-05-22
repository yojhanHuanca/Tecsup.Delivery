package com.Tecsup.dto;

import java.time.LocalDateTime;

public record AuditoriaLogResponseDTO(
        Long id,
        String accion,
        String metodo,
        LocalDateTime fecha,
        String usuario
) {
}
