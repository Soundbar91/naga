package io.naga.pg.domain.apikey.dto.response;

import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.model.ApiKeyStatus;

public record ApiKeyCreateResponse(
    Integer id,
    String publicKey,
    String privateKey,
    String clientKey,
    ApiKeyStatus status
) {

    public static ApiKeyCreateResponse of(ApiKey apiKey, String privateKey) {
        return new ApiKeyCreateResponse(
            apiKey.getId(),
            apiKey.getPublicKey(),
            privateKey,
            apiKey.getClientKey(),
            apiKey.getStatus()
        );
    }
}
