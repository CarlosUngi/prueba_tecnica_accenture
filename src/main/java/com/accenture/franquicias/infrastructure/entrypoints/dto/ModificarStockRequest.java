package com.accenture.franquicias.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModificarStockRequest(
    @NotNull(message = "El stock es obligatorio") @Min(value = 0, message = "El stock debe ser mayor o igual a cero") Integer nuevoStock
) {}