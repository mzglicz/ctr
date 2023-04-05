package org.pl.maciej.ctr.links;

import org.pl.maciej.ctr.links.storage.LinkDocument;
import org.pl.maciej.ctr.links.storage.LinkMongoStorage;
import org.pl.maciej.ctr.links.storage.LinkUpdateDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LinkService {

    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final LinkMongoStorage linkMongoStorage;
    private final UrlProvider urlProvider;

    public LinkService(LinkMongoStorage linkMongoStorage, UrlProvider urlProvider) {
        this.linkMongoStorage = linkMongoStorage;
        this.urlProvider = urlProvider;
    }

    public Mono<LinkResponse> save(LinkRequest linkRequest) {
        var unique = this.urlProvider.generateUniqueIdentifier(linkRequest.target().toString());
        LinkDocument linkDocument = new LinkDocument(linkRequest.target().toString(), unique);
        var result = this.linkMongoStorage.save(linkDocument);
        return result.map(x -> new LinkResponse(x.getId(), x.getTarget(), this.urlProvider.getRelativeUrl(x.getRelativeUrl())));
    }

    public Mono<LinkResponse> getByRelativeUrl(String relativeUrl) {
        return this.linkMongoStorage.get(relativeUrl).map(this::toLinkResponse);
    }

    public Mono<LinkResponse> get(String id) {
        return this.linkMongoStorage.getById(id).map(this::toLinkResponse);
    }

    public Mono<Boolean> update(String id, LinkRequest request) {
        return this.linkMongoStorage.update(id,
                new LinkUpdateDocument(Optional.of(request.target()), Optional.empty())).log().doOnSuccess(element -> log.info(String.valueOf(element))).map(x -> x == 1);
    }

    public Flux<LinkResponse> getAll(int limit, String next) {
        return this.linkMongoStorage.getAll(limit, Optional.ofNullable(next))
                .map(this::toLinkResponse);
    }

    private LinkResponse toLinkResponse(LinkDocument linkDocument) {
        return new LinkResponse(
                linkDocument.getId(),
                linkDocument.getTarget(),
                urlProvider.getAbsolute(linkDocument.getRelativeUrl())
        );
    }

    public Mono<Boolean> delete(String id) {
        return this.linkMongoStorage.deleteById(id).hasElement();
    }
}
