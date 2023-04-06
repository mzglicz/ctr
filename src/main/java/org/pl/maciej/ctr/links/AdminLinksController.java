package org.pl.maciej.ctr.links;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/links")
@Validated
public class AdminLinksController {

    private final LinkService linkService;

    public AdminLinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("")
    Mono<ResponseEntity<LinkResponse> >createLink(@Valid @RequestBody LinkRequest linkRequest) {
        return this.linkService.save(linkRequest).map(response -> ResponseEntity.created(URI.create(response.url())).body(response));
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<LinkResponse>> getLink(@Size(min=1, max=40) @Pattern(regexp = "^[0-9a-zA-Z\\-]+$") @PathVariable("id") String id) {
        return this.linkService.get(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<Void>> updateLink(@Size(min=1, max=40) @Pattern(regexp = "^[0-9a-zA-Z\\-]+$") @PathVariable("id") String id, @RequestBody LinkRequest linkRequest) {
        return this.linkService.update(id, linkRequest)
                .map(matched -> matched ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build());

    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@Size(min=1, max=40) @Pattern(regexp = "^[0-9a-zA-Z\\-]+$") @PathVariable("id") String id) {
        return this.linkService.delete(id)
                .map(x -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .onErrorReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    Mono<LinkAllResponse> getAll(@RequestParam(value = "limit", defaultValue = "10") @Min(10) @Max(100) Integer limit,
                                 @Size(max=40) @Pattern(regexp = "^[0-9a-zA-Z\\-]*$") @RequestParam(value = "next", required = false) String last) {
        return this.linkService.getAll(limit, last).collectList().map(x -> {
            if (!x.isEmpty()) {
                return new LinkAllResponse(x, x.get(x.size() - 1).id());
            } else {
                return new LinkAllResponse(List.of(), "");
            }
        });
    }
}
