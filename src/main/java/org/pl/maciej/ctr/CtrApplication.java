package org.pl.maciej.ctr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
public class CtrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CtrApplication.class, args);
	}

}
