package com.accenture.franquicias.domain.repository;

import com.accenture.franquicias.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    Mono<Franquicia> save(Franquicia franquicia);

    Mono<Franquicia> findById(String id);

    Flux<Franquicia> findAll();
}