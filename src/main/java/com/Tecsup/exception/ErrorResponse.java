package com.Tecsup.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime fecha,
        String mensaje,
        Map<String, String> errores
) {
}
