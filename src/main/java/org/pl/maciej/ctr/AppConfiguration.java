package org.pl.maciej.ctr;

import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AppConfiguration {

    @Bean
    NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> httpServer.accessLog(true)
                .metrics(true, Function.identity());
    }
}
