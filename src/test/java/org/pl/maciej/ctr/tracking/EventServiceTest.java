package org.pl.maciej.ctr.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pl.maciej.ctr.tracking.storage.ClickEventDocument;
import org.pl.maciej.ctr.tracking.storage.EventMongoStorage;

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
        underTest.consumer(clickEvent);
        var captor = ArgumentCaptor.forClass(ClickEventDocument.class);
        verify(storage, times(1)).save(captor.capture());
        assertEquals(captor.getValue().getEventId(), eventId);
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