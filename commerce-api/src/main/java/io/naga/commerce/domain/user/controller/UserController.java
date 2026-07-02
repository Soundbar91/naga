package io.naga.commerce.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.commerce.domain.user.dto.request.UserRegisterRequest;
import io.naga.commerce.domain.user.service.UserService;
import io.naga.common.response.ApiResponse;
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
        return ResponseEntity.ok().build();
    }

}
