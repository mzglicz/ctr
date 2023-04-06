package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("clicks")
public record ClickEventDocument(@Id String id, String eventId, String elementId, Map<String, String> extra) {

    public ClickEventDocument(String eventId, String elementId, Map<String, String> extra) {
        this(null, eventId,elementId,extra);
    }


}
