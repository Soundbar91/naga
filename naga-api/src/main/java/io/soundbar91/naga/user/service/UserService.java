package io.soundbar91.naga.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.soundbar91.naga.common.exception.BusinessException;
import io.soundbar91.naga.common.exception.ErrorCode;
import io.soundbar91.naga.user.entity.User;
import io.soundbar91.naga.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(String email, String rawPassword) {
        validatePassword(rawPassword);

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(email, encodedPassword);

        return userRepository.save(user);
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호는 필수입니다");
        }

        if (rawPassword.length() < 8 || rawPassword.length() > 20) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호는 8~20자여야 합니다");
        }

        if (!rawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).+$")) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
