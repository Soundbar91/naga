package io.soundbar91.naga.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.user.dto.CreateUserRequest;
import io.soundbar91.naga.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User", description = "사용자 API")
public interface UserApi {

    @PostMapping
    @Operation(summary = "회원가입", description = "새로운 사용자를 생성합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검증 실패)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody CreateUserRequest request);
}
