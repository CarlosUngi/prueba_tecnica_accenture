package com.accenture.franquicias.infrastructure.entrypoints.dto;

public record ProductoMayorStockResponse(
    String sucursalNombre,
    String productoNombre,
    Integer stock
) {}