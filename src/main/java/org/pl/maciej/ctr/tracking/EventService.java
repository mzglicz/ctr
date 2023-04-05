package org.pl.maciej.ctr.tracking;


import org.pl.maciej.ctr.tracking.storage.ClickEventDocument;
import org.pl.maciej.ctr.tracking.storage.EventMongoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EventService implements EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventMongoStorage eventMongoStorage;

    public EventService(EventMongoStorage eventMongoStorage) {
        this.eventMongoStorage = eventMongoStorage;
    }

    @Override
    public void consumer(Event event) {
        if ("click".equals(event.actionId())) {
            var document = new ClickEventDocument(event.eventId(), event.elementId(), event.extra());
            this.eventMongoStorage.save(document)
                    .subscribe(x -> log.info("Stored event {} {}", event.eventId(), x.getId()), y -> log.error("Failed to store event {}", event.eventId(), y));
        }
    }

    public Mono<Long> getClickCount() {
        return this.eventMongoStorage.getCount();
    }

    public Flux<TopResult> getTopCount(int limit) {
        return this.eventMongoStorage.getTop(limit)
                .map(x -> new TopResult(x.elementId(), x.count()));

    }
}
