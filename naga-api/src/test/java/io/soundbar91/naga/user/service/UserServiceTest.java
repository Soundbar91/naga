package io.soundbar91.naga.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.soundbar91.naga.common.exception.BusinessException;
import io.soundbar91.naga.user.entity.User;
import io.soundbar91.naga.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        // given
        String email = "test@example.com";
        String rawPassword = "Password123!";
        String encodedPassword = "encodedPassword123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.create(email, rawPassword);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void 회원가입_실패_이메일중복() {
        // given
        String email = "test@example.com";
        String password = "Password123!";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);

        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호가 null")
    void 회원가입_실패_비밀번호null() {
        // given
        String email = "test@example.com";
        String password = null;

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호가 빈 문자열")
    void 회원가입_실패_비밀번호빈문자열() {
        // given
        String email = "test@example.com";
        String password = "";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 길이 8자 미만")
    void 회원가입_실패_비밀번호짧음() {
        // given
        String email = "test@example.com";
        String password = "Pass1!";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 길이 20자 초과")
    void 회원가입_실패_비밀번호김() {
        // given
        String email = "test@example.com";
        String password = "Password1!Password1!Extra";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 형식 오류 (영문 미포함)")
    void 회원가입_실패_비밀번호영문미포함() {
        // given
        String email = "test@example.com";
        String password = "12345678!";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 형식 오류 (숫자 미포함)")
    void 회원가입_실패_비밀번호숫자미포함() {
        // given
        String email = "test@example.com";
        String password = "Password!";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 형식 오류 (특수문자 미포함)")
    void 회원가입_실패_비밀번호특수문자미포함() {
        // given
        String email = "test@example.com";
        String password = "Password123";

        // when & then
        assertThatThrownBy(() -> userService.create(email, password))
                .isInstanceOf(BusinessException.class);
    }
}
