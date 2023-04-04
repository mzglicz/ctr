package org.pl.maciej.ctr.tracking.springbased;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pl.maciej.ctr.tracking.EventProducer;
import org.pl.maciej.ctr.tracking.EventService;
import org.pl.maciej.ctr.tracking.LogEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TrackingConfiguration.class);

    @Bean
    EventProducer springBasedPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringBasedPublisher(applicationEventPublisher);
    }

    @Bean
    SpringListener loggingConsumer(ObjectMapper objectMapper) {
        return new SpringListener(new LogEventConsumer(objectMapper));
    }

    @Bean
    SpringListener storageConsumer(EventService storageEventConsumer) {
        return new SpringListener(storageEventConsumer);
    }
}
