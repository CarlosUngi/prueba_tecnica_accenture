package com.accenture.franquicias.infrastructure.entrypoints;

import com.accenture.franquicias.application.usecase.AdministrarFranquiciaUseCase;
import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final AdministrarFranquiciaUseCase useCase;

    public FranquiciaController(AdministrarFranquiciaUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaDTO> agregarFranquicia(@Valid @RequestBody FranquiciaDTO dto) {
        Franquicia franquiciaDominio = new Franquicia(dto.id(), dto.nombre(), new ArrayList<>());
        return useCase.agregarFranquicia(franquiciaDominio)
                .map(f -> new FranquiciaDTO(f.id(), f.nombre(), new ArrayList<>()));
    }
}