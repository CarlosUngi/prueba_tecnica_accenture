package com.accenture.franquicias.infrastructure.adapters.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "franquicias")
public class FranquiciaDocument {
    @Id
    private String id;
    private String nombre;
    private List<SucursalEntity> sucursales;

    public FranquiciaDocument() {}

    public FranquiciaDocument(String id, String nombre, List<SucursalEntity> sucursales) {
        this.id = id;
        this.nombre = nombre;
        this.sucursales = sucursales;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<SucursalEntity> getSucursales() { return sucursales; }
    public void setSucursales(List<SucursalEntity> sucursales) { this.sucursales = sucursales; }

    public static class SucursalEntity {
        private String id;
        private String nombre;
        private List<ProductoEntity> productos;

        public SucursalEntity() {}
        public SucursalEntity(String id, String nombre, List<ProductoEntity> productos) {
            this.id = id;
            this.nombre = nombre;
            this.productos = productos;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public List<ProductoEntity> getProductos() { return productos; }
        public void setProductos(List<ProductoEntity> productos) { this.productos = productos; }
    }

    public static class ProductoEntity {
        private String id;
        private String nombre;
        private Integer stock;

        public ProductoEntity() {}
        public ProductoEntity(String id, String nombre, Integer stock) {
            this.id = id;
            this.nombre = nombre;
            this.stock = stock;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
    }
}