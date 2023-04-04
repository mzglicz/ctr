package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMongoStorage {

    public static final String CLICKS = "clicks";
    public static final String ELEMENT_ID = "elementId";
    private final MongoOperations mongoOperations;

    public EventMongoStorage(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ClickEventDocument save(ClickEventDocument eventDocument) {
        return this.mongoOperations.save(eventDocument);
    }

    public long getCount() {
        return this.mongoOperations.count(new Query(), CLICKS);
    }

    public List<TopResultDocument> getTop(long limit){
        var groupStage = Aggregation.group(ELEMENT_ID).count().as("count");
        var sortStage = Aggregation.sort(Sort.Direction.DESC, "count");
        var l = Aggregation.limit(limit);
        var obj = this.mongoOperations.aggregate(Aggregation.newAggregation(groupStage, sortStage, l), CLICKS, TopResultDocument.class);
        return obj.getMappedResults();
    }
}
