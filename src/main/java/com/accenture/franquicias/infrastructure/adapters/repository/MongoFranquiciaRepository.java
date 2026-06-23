package com.accenture.franquicias.infrastructure.adapters.repository;

import com.accenture.franquicias.infrastructure.adapters.entity.FranquiciaDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFranquiciaRepository extends ReactiveMongoRepository<FranquiciaDocument, String> {
}