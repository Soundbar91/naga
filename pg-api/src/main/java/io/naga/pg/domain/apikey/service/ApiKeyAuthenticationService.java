package io.naga.pg.domain.apikey.service;

import static io.naga.common.error.ErrorCode.UNAUTHORIZED;
import static io.naga.pg.domain.apikey.model.ApiKeyStatus.ACTIVE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;

// TODO. 클래스 네이밍과 로직 고민해보기.
@Service
@RequiredArgsConstructor
public class ApiKeyAuthenticationService {

    private static final String BASIC_AUTH_SCHEME = "Basic ";

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public Integer authenticate(String authorization) {
        if (!authorization.regionMatches(true, 0, BASIC_AUTH_SCHEME, 0, BASIC_AUTH_SCHEME.length())) {
            throw BusinessException.of(UNAUTHORIZED, "Basic authorization is required");
        }

        String credentials;
        try {
            String encodedCredentials = authorization.substring(BASIC_AUTH_SCHEME.length()).trim();
            credentials = new String(Base64.getDecoder().decode(encodedCredentials), UTF_8);
        } catch (IllegalArgumentException exception) {
            throw BusinessException.of(UNAUTHORIZED, "Basic authorization is invalid");
        }

        int separatorIndex = credentials.indexOf(':');
        if (separatorIndex <= 0 || separatorIndex != credentials.length() - 1) {
            throw BusinessException.of(UNAUTHORIZED, "Basic authorization is invalid");
        }

        String privateKey = credentials.substring(0, separatorIndex);
        ApiKey apiKey = apiKeyRepository.findAllByStatus(ACTIVE).stream()
            .filter(candidate -> passwordEncoder.matches(privateKey, candidate.getPrivateKey()))
            .findFirst()
            .orElseThrow(() -> BusinessException.of(UNAUTHORIZED, "API key credentials are invalid"));

        return apiKey.getUser().getId();
    }
}
