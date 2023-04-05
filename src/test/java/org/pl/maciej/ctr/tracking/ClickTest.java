package org.pl.maciej.ctr.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.pl.maciej.ctr.TestBase;
import org.pl.maciej.ctr.links.LinkResponse;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClickTest extends TestBase {

    private static final String TARGET = "https://www.nba.com";
    private static final Duration TO_WAIT = Duration.ofSeconds(1000);

    @BeforeEach
    public void beforeEach() {
        this.setupDB();
    }

    @Test
    public void countsGetUpdatedProperly() {
        int n = 10;
        var response1 = createLink(TARGET);
        assertCountEquals(0);
        execute(response1.url());
        assertCountEquals(1);
        IntStream.range(0, n).forEach(x -> {
            execute(response1.url());
        });
        assertCountEquals(n+1);
    }

    @Test
    public void shouldGetAggregations() {
        assertCountEquals(0);
        int n = 12;
        var links = IntStream.range(0,n).boxed().map(x -> createLink(TARGET)).collect(Collectors.toList());
        for (int i = 0; i < links.size(); i++) {
            for (int k = 0; k<= i; k++) {
                execute(links.get(i).url());
            }
        }
        assertCountEquals((n*(n+1))/2);
        var topItem = links.get(links.size()-1);
        await()
                .atMost(TO_WAIT)
                .ignoreException(AssertionFailedError.class)
                .until(() -> {
                    var topResults = getTopResult();
                    assertEquals(10, topResults.items().size());
                    assertEquals(topItem.id(), topResults.items().get(0).elementId());
                    assertEquals(n, topResults.items().get(0).count());
                    return true;
                });
    }

    private long getCount() {
        var result = webTestClient
                .get()
                .uri("/stats/count")
                .exchange()
                .returnResult(CountResult.class)
                .getResponseBody().blockFirst();
        return result.count();
    }

    private void assertCountEquals(int expected) {
        await(String.format("Get count expected  %s", expected))
                .atMost(TO_WAIT)
                .until( () -> expected == getCount());

    }
    private void execute(String url) {
        webTestClient.get().uri(url).exchange().expectStatus().isEqualTo(307)
                .returnResult(LinkResponse.class).getResponseBody().blockLast();
    }

    private TopResultResponse getTopResult() {
        return webTestClient.get().uri("/stats/top").exchange()
                .expectStatus()
                .isEqualTo(200)
                .returnResult(TopResultResponse.class)
                .getResponseBody()
                .blockFirst();
    }
}
