package io.naga.pg.domain.apikey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.common.response.ApiResponse;
import io.naga.pg.domain.apikey.dto.response.ApiKeyCreateResponse;
import io.naga.pg.domain.apikey.service.ApiKeyService;
import io.naga.pg.global.auth.UserId;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiResponse<ApiKeyCreateResponse>> createApiKey(
        @UserId Integer userId
    ) {
        ApiKeyCreateResponse response = apiKeyService.createApiKey(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
