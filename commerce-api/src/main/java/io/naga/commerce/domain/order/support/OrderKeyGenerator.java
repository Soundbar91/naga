package io.naga.commerce.domain.order.support;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class OrderKeyGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
