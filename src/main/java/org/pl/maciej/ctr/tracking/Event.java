package org.pl.maciej.ctr.tracking;

import java.util.Map;

public record Event(String eventId, String elementId, String actionId, Map<String, String> extra) {}
