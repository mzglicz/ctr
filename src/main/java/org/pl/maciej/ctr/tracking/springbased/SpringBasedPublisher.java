package org.pl.maciej.ctr.tracking.springbased;

import org.pl.maciej.ctr.tracking.Event;
import org.pl.maciej.ctr.tracking.EventProducer;
import org.springframework.context.ApplicationEventPublisher;

public class SpringBasedPublisher implements EventProducer {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringBasedPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Event event) {
        var spe = new SpringEvent(this, event);
        applicationEventPublisher.publishEvent(spe);
    }
}
