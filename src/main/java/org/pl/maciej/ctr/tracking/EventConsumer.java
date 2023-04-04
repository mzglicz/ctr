package org.pl.maciej.ctr.tracking;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventConsumer {
    void consumer(Event event);
}
