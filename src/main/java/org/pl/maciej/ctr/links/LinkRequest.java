package org.pl.maciej.ctr.links;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkRequest(@URL String target) {
}
