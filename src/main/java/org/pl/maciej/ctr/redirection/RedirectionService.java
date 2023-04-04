package org.pl.maciej.ctr.redirection;

import org.pl.maciej.ctr.links.LinkResponse;
import org.pl.maciej.ctr.links.LinkService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedirectionService {

    private final LinkService linkService;

    private final EventProducerHelper eventProducerHelper;


    public RedirectionService(LinkService linkService, EventProducerHelper eventProducerHelper) {
        this.linkService = linkService;
        this.eventProducerHelper = eventProducerHelper;
    }

    public Optional<LinkResponse> getRedirection(String url) {
        var response = this.linkService.getByRelativeUrl(url);
        response.ifPresent(eventProducerHelper::registerClick);
        return response;
    }

}
