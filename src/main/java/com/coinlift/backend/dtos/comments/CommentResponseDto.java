package com.coinlift.backend.dtos.comments;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CommentResponseDto(
        UUID id,
        String content,
        LocalDateTime createdAt
) {
}