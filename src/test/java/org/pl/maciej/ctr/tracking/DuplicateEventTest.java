package org.pl.maciej.ctr.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pl.maciej.ctr.TestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Duration;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
public class DuplicateEventTest extends TestBase {

    @Autowired
    EventService eventService;

    @BeforeEach
    public void setup() {
        this.setupDB();
    }

    @Test
    public void shouldNotStoreDuplicateEvents(CapturedOutput capturedOutput) {
        var eventId = "d50c9220-c8df-45d4-a76b-12f49f75991f";
        var event = new Event(eventId, eventId, "click", Map.of());

        eventService.consumer(event);
        await()
                .atMost(Duration.ofSeconds(1))
                        .until(()-> eventService.getClickCount().block()==1);
        eventService.consumer(event);
        await()
                .atMost(Duration.ofSeconds(1))
                        .until(()->capturedOutput.getOut().contains("Failed to store event"));

    }
}
