package com.accenture.franquicias.application.usecase;

import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}