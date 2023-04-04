package org.pl.maciej.ctr;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.pl.maciej.ctr.links.LinkRequest;
import org.pl.maciej.ctr.links.LinkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LinksTests extends TestBase {

    private static final Logger log = LoggerFactory.getLogger(LinksTests.class);
    private static final String TARGET = "https://www.zilch.com/us/";

    @Test
    public void creationOfLinkReturns_201() {

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
