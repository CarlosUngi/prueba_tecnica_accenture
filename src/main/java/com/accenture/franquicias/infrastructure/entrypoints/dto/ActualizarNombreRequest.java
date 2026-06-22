package com.accenture.franquicias.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;

public record ActualizarNombreRequest(
    @NotBlank(message = "El nuevo nombre no puede estar vacío") String nuevoNombre
) {}