package com.accenture.franquicias.application.usecase;

import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Producto;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;
import com.accenture.franquicias.infrastructure.entrypoints.dto.ProductoMayorStockResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdministrarFranquiciaUseCase {

    private final FranquiciaRepository repository;

    public AdministrarFranquiciaUseCase(FranquiciaRepository repository) {
        this.repository = repository;
    }

    public Mono<Franquicia> buscarFranquiciaPorId(String franquiciaId) {
        return repository.findById(franquiciaId);
    }

    public Flux<Franquicia> listarFranquicias() {
        return repository.findAll();
    }

    public Mono<Franquicia> agregarFranquicia(Franquicia franquicia) {
        String id = franquicia.id() != null ? franquicia.id() : UUID.randomUUID().toString();
        List<Sucursal> sucursales = franquicia.sucursales() != null ? franquicia.sucursales() : new ArrayList<>();
        Franquicia nueva = new Franquicia(id, franquicia.nombre(), sucursales);
        return repository.save(nueva);
    }

    public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal nuevaSucursal) {

        Mono<Franquicia> franquiciaGuardada = repository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("La franquicia con ID " + franquiciaId + " no existe.")));

        return franquiciaGuardada.flatMap(franquicia -> {
            List<Sucursal> listaSucursales = new ArrayList<>(
                    franquicia.sucursales() != null ? franquicia.sucursales() : List.of());

            Sucursal sucursalConId = new Sucursal(
                    nuevaSucursal.id() != null ? nuevaSucursal.id() : UUID.randomUUID().toString(),
                    nuevaSucursal.nombre(),
                    nuevaSucursal.productos() != null ? nuevaSucursal.productos() : new ArrayList<>());

            listaSucursales.add(sucursalConId);

            Franquicia franquiciaActualizada = new Franquicia(franquicia.id(), franquicia.nombre(), listaSucursales);
            return repository.save(franquiciaActualizada);
        });
    }

    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto nuevoProducto) {
        return repository.findById(franquiciaId)
                .switchIfEmpty(Mono
                        .error(new IllegalArgumentException("La franquicia con ID " + franquiciaId + " no existe.")))
                .flatMap(franquicia -> {
                    List<Sucursal> sucursalesModificadas = franquicia.sucursales().stream().map(sucursal -> {
                        if (sucursal.id().equals(sucursalId)) {
                            List<Producto> productosActualizados = new ArrayList<>(
                                    sucursal.productos() != null ? sucursal.productos() : List.of());

                            Producto productoConId = new Producto(
                                    nuevoProducto.id() != null ? nuevoProducto.id() : UUID.randomUUID().toString(),
                                    nuevoProducto.nombre(),
                                    nuevoProducto.stock());
                            productosActualizados.add(productoConId);
                            return new Sucursal(sucursal.id(), sucursal.nombre(), productosActualizados);
                        }
                        return sucursal;
                    }).collect(java.util.stream.Collectors.toList());

                    Franquicia franquiciaActualizada = new Franquicia(franquicia.id(), franquicia.nombre(),
                            sucursalesModificadas);
                    return repository.save(franquiciaActualizada);
                });
    }

    public Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        Mono<Franquicia> franquiciaAbuscar = repository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("La franquicia con ID " + franquiciaId + " no existe.")));

        return franquiciaAbuscar.flatMap(franquicia -> {
            List<Sucursal> sucursalesModificadas = franquicia.sucursales().stream().map(sucursal -> {
                if (sucursal.id().equals(sucursalId)) {
                    List<Producto> productosFiltrados = sucursal.productos().stream()
                            .filter(p -> !p.id().equals(productoId))
                            .collect(Collectors.toList());
                    return new Sucursal(sucursal.id(), sucursal.nombre(), productosFiltrados);
                }
                return sucursal;
            }).collect(Collectors.toList());

            Franquicia franquiciaActualizada = new Franquicia(franquicia.id(), franquicia.nombre(),
                    sucursalesModificadas);
            return repository.save(franquiciaActualizada);
        });
    }

    public Mono<Franquicia> modificarStock(String franquiciaId, String sucursalId, String productoId,
            Integer nuevoStock) {
        return repository.findById(franquiciaId)
                .switchIfEmpty(Mono
                        .error(new IllegalArgumentException("La franquicia con ID " + franquiciaId + " no existe.")))
                .flatMap(franquicia -> {
                    List<Sucursal> sucursalesModificadas = franquicia.sucursales().stream().map(sucursal -> {
                        if (sucursal.id().equals(sucursalId)) {
                            List<Producto> productosModificados = sucursal.productos().stream().map(producto -> {
                                if (producto.id().equals(productoId)) {
                                    return producto.conStock(nuevoStock);
                                }
                                return producto;
                            }).collect(Collectors.toList());
                            return new Sucursal(sucursal.id(), sucursal.nombre(), productosModificados);
                        }
                        return sucursal;
                    }).collect(Collectors.toList());

                    Franquicia franquiciaActualizada = new Franquicia(franquicia.id(), franquicia.nombre(),
                            sucursalesModificadas);
                    return repository.save(franquiciaActualizada);
                });
    }

    public Flux<ProductoMayorStockResponse> obtenerProductosMaximoStock(String franquiciaId) {
        return repository.findById(franquiciaId)
                .switchIfEmpty(Mono
                        .error(new IllegalArgumentException("La franquicia con ID " + franquiciaId + " no existe.")))
                .flatMapMany(franquicia -> Flux
                        .fromIterable(franquicia.sucursales() != null ? franquicia.sucursales() : List.of()))
                .flatMap(sucursal -> {
                    List<Producto> listaProductos = sucursal.productos() != null ? sucursal.productos() : List.of();
                    if (listaProductos.isEmpty()) {
                        return Mono.empty();
                    }
                    return Flux.fromIterable(listaProductos)
                            .reduce((p1, p2) -> p1.stock() >= p2.stock() ? p1 : p2)
                            .map(prodMax -> new ProductoMayorStockResponse(
                                    sucursal.nombre(),
                                    prodMax.nombre(),
                                    prodMax.stock()));
                });
    }
}