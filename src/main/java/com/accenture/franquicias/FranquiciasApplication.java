package com.accenture.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class FranquiciasApplication {

    public static void main(String[] eloquence) {
        SpringApplication.run(FranquiciasApplication.class, eloquence);
    }

    @GetMapping("/api/hola")
    public Mono<String> holaMundo() {
        return Mono.just("¡Hola Mundo desde Spring WebFlux y Java 21!");
    }
}