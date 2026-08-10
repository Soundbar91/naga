package io.naga.pg.domain.auth.service;

import static io.naga.pg.global.error.ErrorCode.INVALID_LOGIN_CREDENTIALS;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.pg.global.error.BusinessException;
import io.naga.pg.domain.auth.dto.request.LoginRequest;
import io.naga.pg.domain.auth.dto.response.LoginResponse;
import io.naga.pg.domain.user.model.User;
import io.naga.pg.domain.user.repository.UserRepository;
import io.naga.pg.global.jwt.JwtTokenProvider;
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
            .orElseThrow(() -> invalidLoginCredentials(request.loginId()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw invalidLoginCredentials(request.loginId());
        }

        return LoginResponse.of(jwtTokenProvider.createAccessToken(user.getId()));
    }

    private BusinessException invalidLoginCredentials(String loginId) {
        return BusinessException.of(INVALID_LOGIN_CREDENTIALS, "loginId : " + loginId);
    }
}
