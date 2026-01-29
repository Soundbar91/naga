package io.soundbar91.naga.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.user.dto.CreateUserRequest;
import io.soundbar91.naga.user.dto.UserResponse;
import io.soundbar91.naga.user.entity.User;
import io.soundbar91.naga.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "사용자 API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원가입", description = "새로운 사용자를 생성합니다")
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody CreateUserRequest request) {
        User user = userService.create(request.email(), request.password());
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
