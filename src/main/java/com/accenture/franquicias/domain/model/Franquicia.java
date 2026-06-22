package com.accenture.franquicias.domain.model;

import java.util.List;

public record Franquicia(
    String id,
    String nombre,
    List<Sucursal> sucursales
) {
    public Franquicia conNombre(String nuevoNombre) {
        return new Franquicia(this.id, nuevoNombre, this.sucursales);
    }
}