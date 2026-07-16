package io.naga.pg.domain.user.service;

import static io.naga.common.error.ErrorCode.DUPLICATE_LOGIN_ID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.user.dto.request.UserRegisterRequest;
import io.naga.pg.domain.user.model.User;
import io.naga.pg.domain.user.repository.UserRepository;
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
            throw duplicateLoginId(request.loginId());
        }

        User user = User.create(request.loginId(), passwordEncoder.encode(request.password()));
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw duplicateLoginId(request.loginId());
        }
    }

    private BusinessException duplicateLoginId(String loginId) {
        return BusinessException.of(DUPLICATE_LOGIN_ID, "loginId : " + loginId);
    }
}
