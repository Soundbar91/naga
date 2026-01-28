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
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(email, encodedPassword);

        return userRepository.save(user);
    }
}
