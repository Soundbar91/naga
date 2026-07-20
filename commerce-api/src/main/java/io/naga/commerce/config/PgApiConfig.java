package io.naga.commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PgApiConfig {

    @Bean
    public RestClient pgRestClient(
        RestClient.Builder builder,
        @Value("${pg.api.base-url}") String baseUrl,
        @Value("${pg.api.private-key}") String privateKey
    ) {
        return builder
            .baseUrl(baseUrl)
            .defaultHeaders(headers -> headers.setBasicAuth(privateKey, ""))
            .build();
    }
}
