package com.accenture.franquicias.domain.model;

public record Producto(
    String id,
    String nombre,
    Integer stock
) {
    public Producto conStock(Integer nuevoStock) {
        return new Producto(this.id, this.nombre, nuevoStock);
    }

    public Producto conNombre(String nuevoNombre) {
        return new Producto(this.id, nuevoNombre, this.stock);
    }
}