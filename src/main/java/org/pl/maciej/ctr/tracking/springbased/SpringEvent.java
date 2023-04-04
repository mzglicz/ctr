package org.pl.maciej.ctr.tracking.springbased;

import org.pl.maciej.ctr.tracking.Event;
import org.springframework.context.ApplicationEvent;

public class SpringEvent extends ApplicationEvent {
    private final Event event;

    public SpringEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}
