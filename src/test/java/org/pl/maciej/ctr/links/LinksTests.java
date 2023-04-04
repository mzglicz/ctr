package org.pl.maciej.ctr.links;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.pl.maciej.ctr.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LinksTests extends TestBase {

    private static final Logger log = LoggerFactory.getLogger(LinksTests.class);
    private static final String TARGET = "https://www.zilch.com/us/";

    @Test
    public void shouldReturn_201() {

        var validatedResponse = createLink(TARGET);
        assertNotNull(validatedResponse, "Response cannot be null");
        assertTrue(Strings.isNotBlank(validatedResponse.id()), "Id cannot be null");
        // must be valid - this could be improved
        assertTrue(Strings.isNotBlank(validatedResponse.url()), "url");
        assertEquals(TARGET, validatedResponse.target());
    }

    @ParameterizedTest
    @ValueSource(strings = {"!","<",">","some-ziom@", "/relative/url"})
    public void shouldReturn400OnCreateLinkOnMalformedUrl(String target) {
        webTestClient
                .post()
                .uri("/admin/links")
                .bodyValue(new LinkRequest(target))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .returnResult(LinkResponse.class)
                .getResponseBody().blockFirst();
    }

    @Test
    public void getLinkReturnsLink() {
        var creation = createLink(TARGET);
        var uri = getLinkURI(creation.id());
        var response = webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBody(LinkResponse.class)
                .returnResult().getResponseBody();

        assertNotNull(response, "Response cannot be null");
        assertTrue(Strings.isNotBlank(response.id()), "Id cannot be null");
        // must be valid - this could be improved
        assertTrue(Strings.isNotBlank(response.url()), "url");
        assertEquals(TARGET, response.target());
    }

    @Test
    public void gettingNonExistentLinkReturns_404() {
        var uri = getLinkURI("some-string");
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isEqualTo(404)
                .expectBody(LinkResponse.class);
    }

    @Test
    public void deletionOfLinkReturns_204() {
        var response = createLink(TARGET);
        var uri = getLinkURI(response.id());
        log.info("URI is " + uri);

        webTestClient
                .delete()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isEqualTo(204);

        webTestClient
                .delete()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isEqualTo(404);
    }

    @Test
    public void updateWorks() throws MalformedURLException {
        var newTarget = "https://onet.pl";
        var response = createLink(TARGET);
        var uri = getLinkURI(response.id());

        webTestClient
                .put()
                .uri(uri)
                .bodyValue(new LinkRequest(newTarget))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectBody()
                .jsonPath("$.target", newTarget);
    }

    @Test
    public void getAllElementsPaginationWorks() {
        clearDB();
        int n = 2;
        int limit = 10;
        var links = IntStream.range(0, limit * n).boxed().map(i -> createLink(TARGET+i))
                .collect(Collectors.toList());

        var firstResponse = webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/admin/links")
                                .queryParam("limit", limit)
                                .build()
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LinkAllResponse.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(firstResponse);
        assertNotNull(firstResponse.next());
        assertEquals(limit, firstResponse.items().size());
        assertNotEquals("", firstResponse.next());

        var secondResponse =  webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/admin/links")
                                .queryParam("limit", limit)
                                .queryParam("next", firstResponse.next())
                                .build()
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LinkAllResponse.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(secondResponse);
        assertNotNull(secondResponse.next());
        assertEquals(limit, secondResponse.items().size());

        var ids = links.stream().map(LinkResponse::id).sorted().collect(Collectors.toList());
        var responseIds = Stream.concat(firstResponse.items().stream(), secondResponse.items()
                .stream()).map(LinkResponse::id).sorted().collect(Collectors.toList());
        assertEquals(ids, responseIds);
    }

    @Test
    public void updatingNonExistentElementReturns_404() throws MalformedURLException {
        var uri = getLinkURI("some-string");
        webTestClient.put()
                .uri(uri)
                .bodyValue(new LinkRequest(TARGET))
                .exchange()
                .expectStatus()
                .isEqualTo(404)
                .expectBody(LinkResponse.class);
    }

    private String getLinkURI(String id) {
        return UriComponentsBuilder.fromUriString("/admin/links/{id}")
                .build(Map.of("id", id)).toString();
    }
}
