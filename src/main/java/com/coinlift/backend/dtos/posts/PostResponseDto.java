package com.coinlift.backend.dtos.posts;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PostResponseDto(
        UUID uuid,
        String title,
        String content,
        String imageLink,
        LocalDateTime createdAt
) {
}
