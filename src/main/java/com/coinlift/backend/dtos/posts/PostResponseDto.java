package com.coinlift.backend.dtos.posts;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponseDto(
        String title,
        String content,
        String imageLink,
        LocalDateTime createdAt
) {
}
