package org.pl.maciej.ctr.tracking.storage;


import org.springframework.data.annotation.Id;

public record TopResultDocument(@Id String elementId, long count) {
}
