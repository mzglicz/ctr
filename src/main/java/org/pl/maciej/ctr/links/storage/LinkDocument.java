package org.pl.maciej.ctr.links.storage;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("links")
public record LinkDocument(@Id String id, String target, String relativeUrl) {

    public LinkDocument(String target, String relativeUrl) {
        this(null, target, relativeUrl);
    }

}
