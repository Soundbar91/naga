package io.naga.pg.domain.apikey.dto.response;

import java.time.LocalDateTime;

import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.model.ApiKeyStatus;

public record ApiKeyResponse(
    Integer id,
    String clientKey,
    ApiKeyStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ApiKeyResponse of(ApiKey apiKey) {
        return new ApiKeyResponse(
            apiKey.getId(),
            apiKey.getClientKey(),
            apiKey.getStatus(),
            apiKey.getCreatedAt(),
            apiKey.getUpdatedAt()
        );
    }
}
