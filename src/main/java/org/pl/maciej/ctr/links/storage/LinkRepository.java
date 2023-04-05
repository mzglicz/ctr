package org.pl.maciej.ctr.links.storage;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LinkRepository extends ReactiveMongoRepository<LinkDocument, String>, CustomLinkRepository {

    Mono<LinkDocument> findByRelativeUrl(String relativeUrl);

}
