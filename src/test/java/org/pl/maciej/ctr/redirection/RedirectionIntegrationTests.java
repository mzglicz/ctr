package org.pl.maciej.ctr.redirection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.pl.maciej.ctr.TestBase;
import org.springframework.http.HttpHeaders;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RedirectionIntegrationTests extends TestBase {

    @Test
    public void shouldReturnLinkIfPresent() {
        var target = "https://www.nba.com";
        var link = createLink(target);

            webTestClient
                    .get()
                    .uri(link.url())
                    .exchange()
                    .expectStatus()
                    .isEqualTo(307)
                    .expectHeader()
                    .valueEquals(HttpHeaders.LOCATION, target);
        }

        @Test
        public void shouldReturn404LinkIfNotPresent() {
            webTestClient
                    .get()
                    .uri("/links/non-existent-url")
                    .exchange()
                    .expectStatus()
                    .isEqualTo(404);
        }

        // These could be moved to strictly WebMvc tests
        @Test
        public void shouldReturn400WhenIdTooLong() {
            var id = IntStream.range(0,42).boxed().map(x -> "a").collect(Collectors.joining());
            webTestClient
                    .get()
                    .uri("/links/" + id)
                    .exchange()
                    .expectStatus()
                    .isEqualTo(400);
        }

    @ParameterizedTest
    @ValueSource(strings = {"!","<",">"})
    public void shouldReturn400WhenInvalidCharacter(String id) {
        webTestClient
                .get()
                .uri("/links/" + id)
                .exchange()
                .expectStatus()
                .isEqualTo(400);
    }
}
