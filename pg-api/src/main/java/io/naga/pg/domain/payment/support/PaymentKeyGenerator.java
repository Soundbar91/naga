package io.naga.pg.domain.payment.support;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class PaymentKeyGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
