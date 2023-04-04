package org.pl.maciej.ctr.tracking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventConsumer implements EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(LogEventConsumer.class);

    private final ObjectMapper objectMapper;

    public LogEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void consumer(Event event) {
        try {
            log.info("Consumed {}", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("Not able to serialize event", e);
        }
    }
}
