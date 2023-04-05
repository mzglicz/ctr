package org.pl.maciej.ctr.links;

import org.pl.maciej.ctr.links.storage.LinkDocument;
import org.pl.maciej.ctr.links.storage.LinkRepository;
import org.pl.maciej.ctr.links.storage.LinkUpdateDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class LinkService {

    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final UrlProvider urlProvider;

    private final Cache cache;
    private final LinkRepository linkRepository;

    public LinkService(UrlProvider urlProvider, CacheManager cacheManager, LinkRepository linkRepository) {
        this.urlProvider = urlProvider;
        this.cache = cacheManager.getCache("get");
        this.linkRepository = linkRepository;
    }

    public Mono<LinkResponse> save(LinkRequest linkRequest) {
        var unique = this.urlProvider.generateUniqueIdentifier(linkRequest.target());
        LinkDocument linkDocument = new LinkDocument(linkRequest.target(), unique);
        var result = this.linkRepository.save(linkDocument);
        return result.map(x -> new LinkResponse(x.getId(), x.getTarget(), this.urlProvider.getRelativeUrl(x.getRelativeUrl())));
    }

    public Mono<LinkResponse> getByRelativeUrl(String relativeUrl) {
        return Mono.justOrEmpty(cache.get(relativeUrl, LinkResponse.class))
                .switchIfEmpty(Mono.defer(() -> this.linkRepository.findByRelativeUrl(relativeUrl)
                        .map(this::toLinkResponse)
                        .doOnSuccess(x -> cache.put(relativeUrl,x))));
    }

    public Mono<LinkResponse> get(String id) {
        return this.linkRepository.findById(id).map(this::toLinkResponse);
    }

    public Mono<Boolean> update(String id, LinkRequest request) {
        return this.linkRepository.update(id,
                new LinkUpdateDocument(Optional.of(request.target()), Optional.empty())).log().doOnSuccess(element -> log.info(String.valueOf(element))).map(x -> x == 1);
    }

    public Flux<LinkResponse> getAll(int limit, String next) {
        return this.linkRepository.queryForAllWithOffset(limit, Optional.ofNullable(next))
                .map(this::toLinkResponse);
    }

    private LinkResponse toLinkResponse(LinkDocument linkDocument) {
        return new LinkResponse(
                linkDocument.getId(),
                linkDocument.getTarget(),
                urlProvider.getAbsolute(linkDocument.getRelativeUrl())
        );
    }

    public Mono<Void> delete(String id) {
        return this.linkRepository.deleteById(id);
    }
}
