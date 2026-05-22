package com.Tecsup.dto;

import java.math.BigDecimal;

public record DetallePedidoResponseDTO(
        Long id,
        Long productoId,
        String productoNombre,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal
) {
}
