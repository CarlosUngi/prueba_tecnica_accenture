package com.accenture.franquicias.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record FranquiciaDTO(
    String id,
    @NotBlank(message = "El nombre de la franquicia no puede estar vacío") String nombre,
    List<SucursalDTO> sucursales
) {}