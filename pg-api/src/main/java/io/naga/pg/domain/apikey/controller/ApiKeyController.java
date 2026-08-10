package io.naga.pg.domain.apikey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.pg.global.response.ApiResponse;
import io.naga.pg.domain.apikey.dto.response.ApiKeyCreateResponse;
import io.naga.pg.domain.apikey.dto.response.ApiKeyResponse;
import io.naga.pg.domain.apikey.model.ApiKeyStatus;
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

    @PatchMapping("/{apiKeyId}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateApiKey(
        @UserId Integer userId,
        @PathVariable Integer apiKeyId
    ) {
        apiKeyService.deactivateApiKey(userId, apiKeyId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKeyResponse>>> findApiKeys(
        @UserId Integer userId,
        @RequestParam(required = false) ApiKeyStatus status
    ) {
        List<ApiKeyResponse> response = apiKeyService.findApiKeys(userId, status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{apiKeyId}")
    public ResponseEntity<ApiResponse<ApiKeyResponse>> findApiKey(
        @UserId Integer userId,
        @PathVariable Integer apiKeyId
    ) {
        ApiKeyResponse response = apiKeyService.findApiKey(userId, apiKeyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
