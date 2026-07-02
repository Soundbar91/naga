package io.naga.commerce.domain.user.service;

import static io.naga.common.error.ErrorCode.DUPLICATE_LOGIN_ID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.user.dto.request.UserRegisterRequest;
import io.naga.commerce.domain.user.model.User;
import io.naga.commerce.domain.user.repository.UserRepository;
import io.naga.common.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void registerUser(UserRegisterRequest request) {
        if (userRepository.existsByLoginId(request.loginId())) {
            throw BusinessException.of(DUPLICATE_LOGIN_ID, "loginId : " + request.loginId());
        }

        User user = User.create(request.loginId(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }
}
