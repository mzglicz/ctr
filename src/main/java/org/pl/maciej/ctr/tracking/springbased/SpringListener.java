package org.pl.maciej.ctr.tracking.springbased;

import org.pl.maciej.ctr.tracking.EventConsumer;
import org.springframework.context.ApplicationListener;

public class SpringListener implements ApplicationListener<SpringEvent> {

    private final EventConsumer eventConsumer;

    public SpringListener(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void onApplicationEvent(SpringEvent event) {
        this.eventConsumer.consumer(event.getEvent());
    }
}
