package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EventMongoStorage {

    public static final String CLICKS = "clicks";
    public static final String ELEMENT_ID = "elementId";
    private final ReactiveMongoOperations mongoOperations;

    public EventMongoStorage(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Mono<ClickEventDocument> save(ClickEventDocument eventDocument) {
        return this.mongoOperations.save(eventDocument);
    }

    public Mono<Long> getCount() {
        //return this.mongoOperations.exactCount(new Query(), CLICKS);
        return this.mongoOperations.exactCount(new Query(), ClickEventDocument.class, CLICKS);
    }

    public Flux<TopResultDocument> getTop(long limit){
        String count = "count";
        var groupStage = Aggregation.group(ELEMENT_ID).count().as(count);
        var sortStage = Aggregation.sort(Sort.Direction.DESC, count);
        var l = Aggregation.limit(limit);
        return this.mongoOperations.aggregate(Aggregation.newAggregation(groupStage, sortStage, l), CLICKS, TopResultDocument.class);
    }
}
