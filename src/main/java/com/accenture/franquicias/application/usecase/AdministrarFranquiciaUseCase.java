package com.accenture.franquicias.application.usecase;

import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

public class AdministrarFranquiciaUseCase {

    private final FranquiciaRepository repository;

    public AdministrarFranquiciaUseCase(FranquiciaRepository repository) {
        this.repository = repository;
    }

    public Mono<Franquicia> agregarFranquicia(Franquicia franquicia) {
        Franquicia nueva = new Franquicia(
                franquicia.id() != null ? franquicia.id() : UUID.randomUUID().toString(),
                franquicia.nombre(),
                franquicia.sucursales() != null ? franquicia.sucursales() : new ArrayList<>());
        return repository.save(nueva);
    }

}