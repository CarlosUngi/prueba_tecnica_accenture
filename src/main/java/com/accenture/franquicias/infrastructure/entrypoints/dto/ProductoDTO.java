package com.accenture.franquicias.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoDTO(
    String id,
    @NotBlank(message = "El nombre del producto no puede estar vacío") String nombre,
    @NotNull(message = "El stock no puede ser nulo") @Min(value = 0, message = "El stock no puede ser negativo") Integer stock
) {}