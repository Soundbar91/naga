package io.naga.commerce.domain.auth.service;

import static io.naga.common.error.ErrorCode.INVALID_LOGIN_CREDENTIALS;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.auth.dto.request.LoginRequest;
import io.naga.commerce.domain.auth.dto.response.LoginResponse;
import io.naga.commerce.domain.user.model.User;
import io.naga.commerce.domain.user.repository.UserRepository;
import io.naga.commerce.global.jwt.JwtTokenProvider;
import io.naga.common.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> BusinessException.of(INVALID_LOGIN_CREDENTIALS, "loginId : " + request.loginId()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw BusinessException.of(INVALID_LOGIN_CREDENTIALS, "loginId : " + request.loginId());
        }

        return new LoginResponse(jwtTokenProvider.createAccessToken(user.getId()));
    }
}
