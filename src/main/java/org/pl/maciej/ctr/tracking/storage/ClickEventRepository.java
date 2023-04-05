package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickEventRepository extends ReactiveMongoRepository<ClickEventDocument, String>, CustomClickEventRepository {

}
