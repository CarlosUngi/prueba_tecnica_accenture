package com.accenture.franquicias.domain.model;

import java.util.List;

public record Sucursal(
    String id,
    String nombre,
    List<Producto> productos
) {
    public Sucursal conNombre(String nuevoNombre) {
        return new Sucursal(this.id, nuevoNombre, this.productos);
    }
}