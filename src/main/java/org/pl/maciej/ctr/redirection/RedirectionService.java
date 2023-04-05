package org.pl.maciej.ctr.redirection;

import org.pl.maciej.ctr.links.LinkResponse;
import org.pl.maciej.ctr.links.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class RedirectionService {

    private static final Logger log = LoggerFactory.getLogger(RedirectionService.class);

    private final LinkService linkService;

    private final EventProducerHelper eventProducerHelper;


    public RedirectionService(LinkService linkService, EventProducerHelper eventProducerHelper) {
        this.linkService = linkService;
        this.eventProducerHelper = eventProducerHelper;
    }

    public Mono<LinkResponse> getRedirection(String url) {
        var response = this.linkService.getByRelativeUrl(url);
        response.subscribe(eventProducerHelper::registerClick);
        return response;
    }

}
