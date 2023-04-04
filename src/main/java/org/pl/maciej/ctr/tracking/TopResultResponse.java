package org.pl.maciej.ctr.tracking;

import org.pl.maciej.ctr.tracking.storage.TopResultDocument;

import java.util.List;

public record TopResultResponse(List<TopResult> items) {
}
