package org.pl.maciej.ctr

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationListener
import spock.lang.Specification

@SpringBootTest(classes = [CtrApplication, JustATestSpec])
class JustATestSpec extends Specification implements ApplicationListener<ApplicationReadyEvent> {

    static boolean contextSeenStarting = false

    @Override
    void onApplicationEvent(ApplicationReadyEvent event) {
        contextSeenStarting = true
    }

    void "Starts Application Context"() {
        expect:
        contextSeenStarting
    }
}
