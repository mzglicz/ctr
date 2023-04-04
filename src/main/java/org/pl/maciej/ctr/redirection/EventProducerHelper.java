package org.pl.maciej.ctr.redirection;

import org.pl.maciej.ctr.links.LinkResponse;
import org.pl.maciej.ctr.tracking.Event;
import org.pl.maciej.ctr.tracking.EventProducer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class EventProducerHelper {

    private final EventProducer eventProducer;

    public EventProducerHelper(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }
    @Async
    public void registerClick(LinkResponse link) {
        this.eventProducer.publish(
                new Event(UUID.randomUUID().toString(),
                        link.id(),
                        "click",
                        Map.of())
        );
    }
}
