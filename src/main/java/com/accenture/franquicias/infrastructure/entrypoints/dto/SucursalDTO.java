package com.accenture.franquicias.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SucursalDTO(
    String id,
    @NotBlank(message = "El nombre de la sucursal no puede estar vacío") String nombre,
    List<ProductoDTO> productos
) {}