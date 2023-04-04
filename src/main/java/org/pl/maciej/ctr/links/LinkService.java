package org.pl.maciej.ctr.links;

import org.pl.maciej.ctr.links.storage.LinkDocument;
import org.pl.maciej.ctr.links.storage.LinkMongoStorage;
import org.pl.maciej.ctr.links.storage.LinkUpdateDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LinkService {

    private final LinkMongoStorage linkMongoStorage;
    private final UrlProvider urlProvider;

    public LinkService(LinkMongoStorage linkMongoStorage, UrlProvider urlProvider) {
        this.linkMongoStorage = linkMongoStorage;
        this.urlProvider = urlProvider;
    }

    public LinkResponse save(LinkRequest linkRequest) {
        var unique = this.urlProvider.generateUniqueIdentifier(linkRequest.target().toString());
        LinkDocument linkDocument = new LinkDocument(linkRequest.target().toString(), unique);
        var result = this.linkMongoStorage.save(linkDocument);
        return new LinkResponse(result.getId(),
                result.getTarget(),
                 this.urlProvider.getRelativeUrl(result.getRelativeUrl()));
    }

    public Optional<LinkResponse> getByRelativeUrl(String relativeUrl) {
        return this.linkMongoStorage.get(relativeUrl).map(this::toLinkResponse);
    }

    public Optional<LinkResponse> get(String id) {
        return this.linkMongoStorage.getById(id).map(this::toLinkResponse);
    }

    public boolean update(String id, LinkRequest request) {
        return this.linkMongoStorage.update(id,
                new LinkUpdateDocument(Optional.of(request.target()), Optional.empty())) == 1;
    }

    public List<LinkResponse> getAll() {
        return this.linkMongoStorage.getAll().stream()
                .map(this::toLinkResponse)
                .collect(Collectors.toList());
    }

    private LinkResponse toLinkResponse(LinkDocument linkDocument) {
        return new LinkResponse(
                linkDocument.getId(),
                linkDocument.getTarget(),
                urlProvider.getAbsolute(linkDocument.getRelativeUrl())
        );
    }

    public boolean delete(String id) {
        return this.linkMongoStorage.deleteById(id).isPresent();
    }
}
