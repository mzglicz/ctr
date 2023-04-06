package org.pl.maciej.ctr.links.storage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CustomLinkRepository {

    Flux<LinkDocument> queryForAllWithOffset(int limit, Optional<String> next);
    Mono<Long> update(String id, LinkUpdateDocument linkUpdateDocument);
}
