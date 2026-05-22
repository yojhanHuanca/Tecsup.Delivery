package com.Tecsup.dto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        Long categoriaId,
        String categoriaNombre
) {
}
