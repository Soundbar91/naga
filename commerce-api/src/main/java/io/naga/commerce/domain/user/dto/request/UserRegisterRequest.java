package io.naga.commerce.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterRequest(
    @NotEmpty(message = "로그인 ID는 필수입니다.")
    String loginId,

    @NotEmpty(message = "비밀번호는 필수입니다.")
    String password
) {
}
