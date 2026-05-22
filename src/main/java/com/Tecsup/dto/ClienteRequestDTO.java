package com.Tecsup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message = "Los nombres son obligatorios")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        String apellidos,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato valido")
        String correo,

        @NotBlank(message = "El telefono es obligatorio")
        String telefono,

        @NotBlank(message = "La direccion es obligatoria")
        String direccion
) {
}
