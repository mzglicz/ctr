package org.pl.maciej.ctr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ActuatorEndpointsTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void healthcheckEndpointsReturnsOk() {

        webTestClient.get()
                .uri("/ctr/health")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void prometheusEndpointReturnsOk() {
        webTestClient.get()
                .uri("/ctr/prometheus")
                .exchange()
                .expectStatus()
                .isOk();
    }

}
