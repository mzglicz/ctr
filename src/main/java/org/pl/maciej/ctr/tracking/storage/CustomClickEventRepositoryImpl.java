package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

public class CustomClickEventRepositoryImpl implements CustomClickEventRepository {

    public static final String CLICKS = "clicks";
    public static final String ELEMENT_ID = "elementId";
    private final ReactiveMongoOperations mongoOperations;

    public CustomClickEventRepositoryImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Flux<TopResultDocument> getTop(int limit){
        String count = "count";
        var groupStage = Aggregation.group(ELEMENT_ID).count().as(count);
        var sortStage = Aggregation.sort(Sort.Direction.DESC, count);
        var l = Aggregation.limit(limit);
        return this.mongoOperations.aggregate(Aggregation.newAggregation(groupStage, sortStage, l), CLICKS, TopResultDocument.class);
    }
}
