package org.pl.maciej.ctr.links;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfiguration {

    @Bean
    public IdProvider getIdProvider() {
        return (Link) -> UUID.randomUUID().toString();
    }
}
