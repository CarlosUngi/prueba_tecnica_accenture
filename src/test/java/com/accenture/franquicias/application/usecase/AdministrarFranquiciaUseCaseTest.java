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

    @Test
    void agregarProducto_DebeAdicionarProductoASucursalCorrecta() {
        String franquiciaId = "fran-01";
        String sucursalId = "suc-01";

        Sucursal sucursalInicial = new Sucursal(sucursalId, "Sucursal Centro", new ArrayList<>());
        Franquicia franquiciaExistente = new Franquicia(franquiciaId, "Franquicia Bogota", List.of(sucursalInicial));

        com.accenture.franquicias.domain.model.Producto nuevoProducto = new com.accenture.franquicias.domain.model.Producto(
                null, "Empanada", 20);

        when(repository.findById(franquiciaId)).thenReturn(Mono.just(franquiciaExistente));

        Franquicia franquiciaGuardada = new Franquicia(franquiciaId, "Franquicia Bogota",
                List.of(new Sucursal(sucursalId, "Sucursal Centro",
                        List.of(new com.accenture.franquicias.domain.model.Producto("prod-1", "Empanada", 20)))));
        when(repository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaGuardada));

        StepVerifier.create(useCase.agregarProducto(franquiciaId, sucursalId, nuevoProducto))
                .expectNextMatches(f -> {
                    Sucursal s = f.sucursales().get(0);
                    return s.productos().size() == 1 && s.productos().get(0).nombre().equals("Empanada");
                })
                .verifyComplete();
    }

    @Test
    void modificarStock_DebeActualizarCantidadDeUnProducto() {
        String franquiciaId = "fran-01";
        String sucursalId = "suc-01";
        String productoId = "prod-01";

        com.accenture.franquicias.domain.model.Producto productoInicial = new com.accenture.franquicias.domain.model.Producto(
                productoId, "Camisa", 10);
        Sucursal sucursal = new Sucursal(sucursalId, "Sucursal Principal", List.of(productoInicial));
        Franquicia franquicia = new Franquicia(franquiciaId, "Franquicia Textil", List.of(sucursal));

        when(repository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));

        Franquicia franquiciaActualizada = new Franquicia(franquiciaId, "Franquicia Textil",
                List.of(new Sucursal(sucursalId, "Sucursal Principal",
                        List.of(new com.accenture.franquicias.domain.model.Producto(productoId, "Camisa", 50)))));
        when(repository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaActualizada));

        StepVerifier.create(useCase.modificarStock(franquiciaId, sucursalId, productoId, 50))
                .expectNextMatches(f -> f.sucursales().get(0).productos().get(0).stock() == 50)
                .verifyComplete();
    }

    @Test
    void modificarStock_DebeLanzarError_SiFranquiciaNoExiste() {
        when(repository.findById("invalido")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.modificarStock("invalido", "suc-1", "prod-1", 100))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}