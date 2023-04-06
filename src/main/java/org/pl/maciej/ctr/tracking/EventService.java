package org.pl.maciej.ctr.tracking;


import org.pl.maciej.ctr.tracking.storage.ClickEventDocument;
import org.pl.maciej.ctr.tracking.storage.ClickEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EventService implements EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final ClickEventRepository clickEventRepository;

    public EventService(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }

    @Override
    public void consumer(Event event) {
        if ("click".equals(event.actionId())) {
            var document = new ClickEventDocument(event.eventId(), event.elementId(), event.extra());
            this.clickEventRepository.save(document)
                    .subscribe(x -> log.info("Stored event {} {}", event.eventId(), x.id()), y -> log.error("Failed to store event {}", event.eventId(), y));
        }
    }

    public Mono<Long> getClickCount() {
        return this.clickEventRepository.count();
    }

    public Flux<TopResult> getTopCount(int limit) {
        return this.clickEventRepository.getTop(limit)
                .map(x -> new TopResult(x.elementId(), x.count()));

    }
}
