package com.Tecsup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDate fecha,
        BigDecimal total,
        Long clienteId,
        String clienteNombreCompleto,
        List<DetallePedidoResponseDTO> detalles
) {
}
