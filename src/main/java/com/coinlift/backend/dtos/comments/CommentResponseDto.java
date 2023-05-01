package com.coinlift.backend.dtos.comments;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto(
        UUID uuid,
        String content,
        LocalDateTime createdAt
) {
}