package org.pl.maciej.ctr.tracking.storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("clicks")
public class ClickEventDocument {

    @Id
    private String id;

    private String eventId;

    private String elementId;

    private Map<String, String> extra;

    public ClickEventDocument(String eventId, String elementId, Map<String, String> extra) {
        this.eventId = eventId;
        this.elementId = elementId;
        this.extra = extra;
    }

    public ClickEventDocument() {
    }

    public String getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getElementId() {
        return elementId;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

}
