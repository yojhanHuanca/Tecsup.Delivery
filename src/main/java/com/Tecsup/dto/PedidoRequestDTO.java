package com.Tecsup.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        @NotEmpty(message = "El pedido debe contener al menos un producto")
        List<@Valid DetallePedidoRequestDTO> detalles
) {
}
