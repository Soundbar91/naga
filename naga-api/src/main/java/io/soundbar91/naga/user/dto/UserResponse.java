package io.soundbar91.naga.user.dto;

import java.time.LocalDateTime;

import io.soundbar91.naga.user.entity.User;

public record UserResponse(
    Long id,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
