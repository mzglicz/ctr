package org.pl.maciej.ctr.links.storage;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LinkMongoStorage {

    private final MongoOperations mongoOperations;

    public LinkMongoStorage(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public LinkDocument save(LinkDocument document) {
        return this.mongoOperations.save(document);
    }

    public Optional<LinkDocument> get(String relativeUrl) {
        var query = new Query();
        query.addCriteria(Criteria.where("relativeUrl").is(relativeUrl));

        var element = this.mongoOperations.findOne(query, LinkDocument.class, "links");
        return Optional.ofNullable(element);
    }

    public List<LinkDocument> getAll() {
        return this.mongoOperations.findAll(LinkDocument.class, "links");
    }

    public Optional<LinkDocument> getById(String id) {
        return Optional.ofNullable(this.mongoOperations.findById(id, LinkDocument.class, "links"));
    }

    public Optional<LinkDocument> deleteById(String id) {
        var query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        var result = this.mongoOperations.findAndRemove(query, LinkDocument.class, "links");
        return Optional.ofNullable(result);
    }

    public long update(String id, LinkUpdateDocument linkUpdateDocument) {
        var query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        var update = new Update();
        linkUpdateDocument.url().ifPresent(url -> update.set("url", url));
        linkUpdateDocument.target().ifPresent(target -> update.set("target", target));

        return this.mongoOperations.updateFirst(query, update, "links").getMatchedCount();
    }
}
