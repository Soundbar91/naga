package io.soundbar91.naga.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.user.dto.CreateUserRequest;
import io.soundbar91.naga.user.dto.UserResponse;
import io.soundbar91.naga.user.entity.User;
import io.soundbar91.naga.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> create(CreateUserRequest request) {
        User user = userService.create(request.email(), request.password());
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
