package org.pl.maciej.ctr.tracking.storage;

import reactor.core.publisher.Flux;

public interface CustomClickEventRepository {
    Flux<TopResultDocument> getTop(int count);
}
