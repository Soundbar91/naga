package io.naga.pg.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.pg.global.response.ApiResponse;
import io.naga.pg.domain.user.dto.request.UserRegisterRequest;
import io.naga.pg.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerUser(
        @Valid @RequestBody UserRegisterRequest request
    ) {
        userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
