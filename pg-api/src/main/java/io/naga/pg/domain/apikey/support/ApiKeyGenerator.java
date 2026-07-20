package io.naga.pg.domain.apikey.support;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyGenerator {

    private static final int KEY_LENGTH = 32;
    private static final String PRIVATE_KEY_PREFIX = "sk_";
    private static final String CLIENT_KEY_PREFIX = "ck_";

    private final SecureRandom secureRandom = new SecureRandom();

    public String generatePrivateKey() {
        return PRIVATE_KEY_PREFIX + generateRandomValue();
    }

    public String generateClientKey() {
        return CLIENT_KEY_PREFIX + generateRandomValue();
    }

    private String generateRandomValue() {
        byte[] bytes = new byte[KEY_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
