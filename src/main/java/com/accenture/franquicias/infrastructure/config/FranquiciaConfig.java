package com.accenture.franquicias.infrastructure.config;

import com.accenture.franquicias.application.usecase.AdministrarFranquiciaUseCase;
import com.accenture.franquicias.domain.repository.FranquiciaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FranquiciaConfig {

    @Bean
    public AdministrarFranquiciaUseCase administrarFranquiciaUseCase(FranquiciaRepository repository) {
        return new AdministrarFranquiciaUseCase(repository);
    }
}