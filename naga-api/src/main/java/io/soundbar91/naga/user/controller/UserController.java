package io.soundbar91.naga.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.user.dto.CreateUserRequest;
import io.soundbar91.naga.user.dto.UserResponse;
import io.soundbar91.naga.user.entity.User;
import io.soundbar91.naga.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.create(request.email(), request.password());
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
