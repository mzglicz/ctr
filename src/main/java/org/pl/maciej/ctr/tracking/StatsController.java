package org.pl.maciej.ctr.tracking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final EventService eventService;

    public StatsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/count")
    public CountResult getCount() {
        return new CountResult(this.eventService.getClickCount());
    }

    @GetMapping("/top")
    public TopResultResponse getTopResult(@Min(1) @Max(100) @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return new TopResultResponse(this.eventService.getTopCount(limit));
    }
}
