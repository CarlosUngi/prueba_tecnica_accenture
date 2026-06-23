package com.accenture.franquicias.infrastructure.entrypoints;

import com.accenture.franquicias.application.usecase.AdministrarFranquiciaUseCase;
import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Producto;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.infrastructure.entrypoints.dto.ActualizarNombreRequest;
import com.accenture.franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import com.accenture.franquicias.infrastructure.entrypoints.dto.ModificarStockRequest;
import com.accenture.franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import com.accenture.franquicias.infrastructure.entrypoints.dto.ProductoMayorStockResponse;
import com.accenture.franquicias.infrastructure.entrypoints.dto.SucursalDTO;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final AdministrarFranquiciaUseCase useCase;

    public FranquiciaController(AdministrarFranquiciaUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping()
    public Flux<FranquiciaDTO> listarFranquicias() {
        return useCase.listarFranquicias()
                .map(this::mapToDTO);
    }

    @GetMapping("/{franquiciaId}")
    public Mono<FranquiciaDTO> buscarFranquiciaPorId(@PathVariable String franquiciaId) {
        return useCase.buscarFranquiciaPorId(franquiciaId)
                .map(this::mapToDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaDTO> agregarFranquicia(@Valid @RequestBody FranquiciaDTO dto) {
        Franquicia franquiciaDominio = mapToDomain(dto);
        return useCase.agregarFranquicia(franquiciaDominio)
                .map(this::mapToDTO);
    }

    @PostMapping("/{franquiciaId}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaDTO> agregarSucursal(
            @PathVariable String franquiciaId,
            @Valid @RequestBody SucursalDTO sucursalDto) {

        Sucursal sucursalDominio = mapToDomain(sucursalDto);

        return useCase.agregarSucursal(franquiciaId, sucursalDominio)
                .map(this::mapToDTO);
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalId}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaDTO> agregarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @Valid @RequestBody ProductoDTO productoDto) {

        Producto productoDominio = new Producto(productoDto.id(), productoDto.nombre(), productoDto.stock());

        return useCase.agregarProducto(franquiciaId, sucursalId, productoDominio)
                .map(this::mapToDTO);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public Mono<FranquiciaDTO> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {

        return useCase.eliminarProducto(franquiciaId, sucursalId, productoId)
                .map(this::mapToDTO);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public Mono<FranquiciaDTO> modificarStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @Valid @RequestBody ModificarStockRequest request) {

        return useCase.modificarStock(franquiciaId, sucursalId, productoId, request.nuevoStock())
                .map(this::mapToDTO);
    }

    @GetMapping("/{franquiciaId}/max-stock-por-sucursal")
    public Flux<ProductoMayorStockResponse> obtenerProductosMaximoStock(
            @PathVariable String franquiciaId) {
        return useCase.obtenerProductosMaximoStock(franquiciaId);
    }

    @PutMapping("/{franquiciaId}/nombre")
    public Mono<FranquiciaDTO> actualizarNombreFranquicia(
            @PathVariable String franquiciaId,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return useCase.actualizarNombreFranquicia(franquiciaId, request.nuevoNombre())
                .map(this::mapToDTO);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/nombre")
    public Mono<FranquiciaDTO> actualizarNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return useCase.actualizarNombreSucursal(franquiciaId, sucursalId, request.nuevoNombre())
                .map(this::mapToDTO);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre")
    public Mono<FranquiciaDTO> actualizarNombreProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return useCase.actualizarNombreProducto(franquiciaId, sucursalId, productoId, request.nuevoNombre())
                .map(this::mapToDTO);
    }

    private FranquiciaDTO mapToDTO(Franquicia f) {
        if (f == null) {
            return null;
        }
        List<SucursalDTO> sucursales = f.sucursales() == null ? new ArrayList<>()
                : f.sucursales().stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        return new FranquiciaDTO(f.id(), f.nombre(), sucursales);
    }

    private SucursalDTO mapToDTO(Sucursal s) {
        if (s == null) {
            return null;
        }
        List<ProductoDTO> productos = s.productos() == null ? new ArrayList<>()
                : s.productos().stream()
                        .map(p -> new ProductoDTO(p.id(), p.nombre(), p.stock()))
                        .collect(Collectors.toList());
        return new SucursalDTO(s.id(), s.nombre(), productos);
    }

    private Franquicia mapToDomain(FranquiciaDTO dto) {
        if (dto == null) {
            return null;
        }
        List<Sucursal> sucursales = dto.sucursales() == null ? new ArrayList<>()
                : dto.sucursales().stream()
                        .map(this::mapToDomain)
                        .collect(Collectors.toList());
        return new Franquicia(dto.id(), dto.nombre(), sucursales);
    }

    private Sucursal mapToDomain(SucursalDTO dto) {
        if (dto == null) {
            return null;
        }
        List<Producto> productos = dto.productos() == null ? new ArrayList<>()
                : dto.productos().stream()
                        .map(p -> new Producto(p.id(), p.nombre(), p.stock()))
                        .collect(Collectors.toList());
        return new Sucursal(dto.id(), dto.nombre(), productos);
    }
}