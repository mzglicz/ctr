package org.pl.maciej.ctr.links.storage;

import java.util.Optional;

public record LinkUpdateDocument(Optional<String> target, Optional<String> url) {
}
