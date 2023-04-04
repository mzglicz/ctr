package org.pl.maciej.ctr.links;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // Debatable if should return an object
    @PostMapping("")
    ResponseEntity<LinkResponse> createLink(@Valid @RequestBody LinkRequest linkRequest) {
        var response = this.linkService.save(linkRequest);
        return ResponseEntity.created(URI.create(response.url())).body(response);

    }

    @GetMapping("/{id}")
    ResponseEntity<LinkResponse> getLink(@PathVariable("id") String id) {
        return this.linkService.get(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateLink(@PathVariable("id") String id, @RequestBody LinkRequest linkRequest) {
        //return this.linkService.update(id, linkRequest);
        var matched = this.linkService.update(id, linkRequest);
        if (matched) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") String id) {
        boolean deleted = this.linkService.delete(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
        }
    }

    @GetMapping("")
    ResponseEntity<List<LinkResponse>> getAll() {
        return ResponseEntity.ok(this.linkService.getAll());
    }
}
