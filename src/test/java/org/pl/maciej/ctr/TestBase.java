package org.pl.maciej.ctr;

import org.pl.maciej.ctr.links.LinkRequest;
import org.pl.maciej.ctr.links.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestBase {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    ReactiveMongoOperations mongoOperations;

    protected void setupDB() {
        mongoOperations.remove(new Query(), "clicks").block();
        mongoOperations.remove(new Query(), "links").block();
        createIndexes();
    }

    protected void createIndexes() {
        mongoOperations.indexOps("links")
                .ensureIndex(new Index().on("relativeUrl", Sort.Direction.ASC))
                .block();
        mongoOperations.indexOps("clicks")
                .ensureIndex(new Index().on("eventId", Sort.Direction.ASC).unique())
                .block();
    }

    protected LinkResponse createLink(String target) {
        // TODO This should actually return the "location" header - figure out how to use it
            return webTestClient
                    .post()
                    .uri("/admin/links")
                    .bodyValue(new LinkRequest(target))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .returnResult(LinkResponse.class)
                    .getResponseBody().blockFirst();
    }
}
