package org.pl.maciej.ctr.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pl.maciej.ctr.tracking.storage.ClickEventDocument;
import org.pl.maciej.ctr.tracking.storage.EventMongoStorage;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    EventMongoStorage storage;

    EventService underTest;

    @BeforeEach
    void setup() {
        storage = mock(EventMongoStorage.class);
        underTest = new EventService(storage);
    }

    @Test
    void shouldStoreClickEvents() {
        var eventId = "SomeCrazyEvent";
        var clickEvent = new Event(eventId, "elementId", "click", Map.of());
        var captor = ArgumentCaptor.forClass(ClickEventDocument.class);

        Mockito.when(storage.save(captor.capture())).thenReturn(Mono.just(new ClickEventDocument()));
        underTest.consumer(clickEvent);

    }

    @Test
    void shouldSkipOnCLickEvents() {
        var event = new Event("eventId", "elementId", "different-event", Map.of());
        underTest.consumer(event);
        verify(storage, never()).save(any());
    }

    @Test
    void getClickCount() {
        underTest.getClickCount();
        verify(storage, times(1)).getCount();
    }
}