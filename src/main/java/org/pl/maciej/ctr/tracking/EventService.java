package org.pl.maciej.ctr.tracking;


import org.pl.maciej.ctr.tracking.storage.ClickEventDocument;
import org.pl.maciej.ctr.tracking.storage.EventMongoStorage;
import org.pl.maciej.ctr.tracking.storage.TopResultDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
            this.eventMongoStorage.save(document);
            log.info("Stored event {} ", event.eventId());
        }
    }

    public long getClickCount() {
        return this.eventMongoStorage.getCount();
    }

    public List<TopResult> getTopCount(int limit) {
        return this.eventMongoStorage.getTop(limit)
                .stream()
                .map(x -> new TopResult(x.elementId(), x.count()))
                .collect(Collectors.toList());
    }
}
