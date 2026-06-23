package com.accenture.franquicias.infrastructure.adapters;

import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Producto;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;
import com.accenture.franquicias.infrastructure.adapters.entity.FranquiciaDocument;
import com.accenture.franquicias.infrastructure.adapters.repository.MongoFranquiciaRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FranquiciaMongoAdapter implements FranquiciaRepository {

    private final MongoFranquiciaRepository repository;

    public FranquiciaMongoAdapter(MongoFranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return repository.save(toDocument(franquicia))
                .map(this::toDomain);
    }

    @Override
    public Mono<Franquicia> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    private FranquiciaDocument toDocument(Franquicia domain) {
        List<FranquiciaDocument.SucursalEntity> sucursales = domain.sucursales() == null ? new ArrayList<>()
                : domain.sucursales().stream().map(s -> new FranquiciaDocument.SucursalEntity(
                        s.id(), s.nombre(),
                        s.productos() == null ? new ArrayList<>()
                                : s.productos().stream()
                                        .map(p -> new FranquiciaDocument.ProductoEntity(p.id(), p.nombre(), p.stock()))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList());
        return new FranquiciaDocument(domain.id(), domain.nombre(), sucursales);
    }

    private Franquicia toDomain(FranquiciaDocument doc) {
        List<Sucursal> sucursales = doc.getSucursales() == null ? new ArrayList<>()
                : doc.getSucursales().stream().map(s -> new Sucursal(
                        s.getId(), s.getNombre(),
                        s.getProductos() == null ? new ArrayList<>()
                                : s.getProductos().stream()
                                        .map(p -> new Producto(p.getId(), p.getNombre(), p.getStock()))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList());
        return new Franquicia(doc.getId(), doc.getNombre(), sucursales);
    }
}