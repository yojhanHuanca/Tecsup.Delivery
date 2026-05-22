package com.Tecsup.dto;

public record ClienteResponseDTO(
        Long id,
        String nombres,
        String apellidos,
        String correo,
        String telefono,
        String direccion
) {
}
