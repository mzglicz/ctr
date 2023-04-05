package org.pl.maciej.ctr.links;

import java.util.List;

public record LinkAllResponse(List<LinkResponse> items, String next) {
}
