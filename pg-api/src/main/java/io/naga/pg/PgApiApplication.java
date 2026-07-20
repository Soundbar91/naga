package io.naga.pg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PgApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgApiApplication.class, args);
    }

}
