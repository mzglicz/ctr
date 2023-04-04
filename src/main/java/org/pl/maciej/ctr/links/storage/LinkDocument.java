package org.pl.maciej.ctr.links.storage;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("links")
public class LinkDocument {
    @Id
    private String id;

    private String target;
    private String relativeUrl;

    public LinkDocument() {}

    public LinkDocument(String target, String relativeUrl) {
        this.target = target;
        this.relativeUrl = relativeUrl;
    }

    public String getId() {
        return id;
    }

    public String getTarget() {
        return target;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }
}
