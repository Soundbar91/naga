package io.naga.pg.domain.apikey.service;

import static io.naga.common.error.ErrorCode.ACTIVE_API_KEY_ALREADY_EXISTS;
import static io.naga.common.error.ErrorCode.NOT_FOUND_USER;
import static io.naga.pg.domain.apikey.model.ApiKeyStatus.ACTIVE;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.apikey.dto.response.ApiKeyCreateResponse;
import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.repository.ApiKeyRepository;
import io.naga.pg.domain.apikey.support.ApiKeyGenerator;
import io.naga.pg.domain.user.model.User;
import io.naga.pg.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiKeyService {

    private final ApiKeyGenerator apiKeyGenerator;
    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public ApiKeyCreateResponse createApiKey(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_USER, "userId : " + userId));

        if (apiKeyRepository.existsByUserIdAndStatus(userId, ACTIVE)) {
            throw BusinessException.of(ACTIVE_API_KEY_ALREADY_EXISTS, "userId : " + userId);
        }

        String publicKey = apiKeyGenerator.generatePublicKey();
        String privateKey = apiKeyGenerator.generatePrivateKey();
        ApiKey apiKey = ApiKey.create(user, publicKey, passwordEncoder.encode(privateKey));
        ApiKey savedApiKey = apiKeyRepository.save(apiKey);

        return ApiKeyCreateResponse.of(savedApiKey, privateKey);
    }
}
