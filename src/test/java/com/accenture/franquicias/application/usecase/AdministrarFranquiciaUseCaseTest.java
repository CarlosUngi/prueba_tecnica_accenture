package com.accenture.franquicias.application.usecase;

import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdministrarFranquiciaUseCaseTest {

    private FranquiciaRepository repository;
    private AdministrarFranquiciaUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FranquiciaRepository.class);
        useCase = new AdministrarFranquiciaUseCase(repository);
    }

    @Test
    void agregarFranquicia_DebeGuardarExitosamente() {
        Franquicia franquiciaEntrada = new Franquicia(null, "Franquicia Prueba", new ArrayList<>());
        Franquicia franquiciaGuardada = new Franquicia("12345", "Franquicia Prueba", new ArrayList<>());
        when(repository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaGuardada));

        StepVerifier.create(useCase.agregarFranquicia(franquiciaEntrada))
                .expectNextMatches(f -> f.id().equals("12345") && f.nombre().equals("Franquicia Prueba"))
                .verifyComplete();
    }

    @Test
    void agregarSucursal_DebeAdicionarSucursalAFranquiciaExistente() {
        String franquiciaId = "fran-99";
        Franquicia franquiciaExistente = new Franquicia(franquiciaId, "Franquicia Master", new ArrayList<>());
        Sucursal nuevaSucursal = new Sucursal(null, "Sucursal Medellin", new ArrayList<>());

        Franquicia franquiciaActualizada = new Franquicia(franquiciaId, "Franquicia Master",
                List.of(new Sucursal("suc-123", "Sucursal Medellin", new ArrayList<>())));

        when(repository.findById(franquiciaId)).thenReturn(Mono.just(franquiciaExistente));
        when(repository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaActualizada));
        StepVerifier.create(useCase.agregarSucursal(franquiciaId, nuevaSucursal))
                .expectNextMatches(f -> f.sucursales().size() == 1 &&
                        f.sucursales().get(0).nombre().equals("Sucursal Medellin"))
                .verifyComplete();
    }

    @Test
    void agregarSucursal_DebeLanzarError_SiFranquiciaNoExiste() {
        String idInvalido = "id-fantasma";
        Sucursal nuevaSucursal = new Sucursal(null, "Sucursal Error", new ArrayList<>());

        when(repository.findById(idInvalido)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.agregarSucursal(idInvalido, nuevaSucursal))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}