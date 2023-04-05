package org.pl.maciej.ctr.links.storage;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class LinkMongoStorage {

    private final ReactiveMongoOperations
            mongoOperations;

    public LinkMongoStorage(ReactiveMongoOperations
                                    mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Mono<LinkDocument> save(LinkDocument document) {
        return this.mongoOperations.save(document);
    }

    public Mono<LinkDocument> get(String relativeUrl) {
        var query = new Query();
        query.addCriteria(Criteria.where("relativeUrl").is(relativeUrl));

        return this.mongoOperations.findOne(query, LinkDocument.class, "links");
    }

    public Flux<LinkDocument> getAll(int limit, Optional<String> next) {
        Query query = new Query();
        query.limit(limit);
        query.with(Sort.by(Sort.Direction.ASC, "_id"));
        next.ifPresent(value -> query.addCriteria(Criteria.where("_id").gt(new ObjectId(value))));
        return this.mongoOperations.find(query, LinkDocument.class, "links");
    }

    public Mono<LinkDocument> getById(String id) {
        return this.mongoOperations.findById(id, LinkDocument.class, "links");
    }

    public Mono<LinkDocument> deleteById(String id) {
        var query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoOperations.findAndRemove(query, LinkDocument.class, "links");
    }

    public Mono<Long> update(String id, LinkUpdateDocument linkUpdateDocument) {
        var query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        var update = new Update();
        linkUpdateDocument.url().ifPresent(url -> update.set("url", url));
        linkUpdateDocument.target().ifPresent(target -> update.set("target", target));

        return this.mongoOperations.updateFirst(query, update, "links")
                .map(UpdateResult::getMatchedCount);
    }
}
