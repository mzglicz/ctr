package org.pl.maciej.ctr.redirection;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@Validated
public class RedirectionController {
    private RedirectionService redirectionService;

    public RedirectionController(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @GetMapping("/links/{url}")
    public Mono<ResponseEntity<Void>> click(@Size(min=1, max=40) @Pattern(regexp = "^[0-9a-zA-Z\\-]+$") @PathVariable("url") String url) {
        return this.redirectionService.getRedirection(url)
                .map(x -> ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                        .location(URI.create(x.target())))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)).map(ResponseEntity.HeadersBuilder::build);
    }
}
