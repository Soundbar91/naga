package io.naga.pg.domain.apikey.support;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyGenerator {

    private static final int KEY_LENGTH = 32;
    private static final String PUBLIC_KEY_PREFIX = "pk_";
    private static final String PRIVATE_KEY_PREFIX = "sk_";

    private final SecureRandom secureRandom = new SecureRandom();

    public String generatePublicKey() {
        return PUBLIC_KEY_PREFIX + generateRandomValue();
    }

    public String generatePrivateKey() {
        return PRIVATE_KEY_PREFIX + generateRandomValue();
    }

    private String generateRandomValue() {
        byte[] bytes = new byte[KEY_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
