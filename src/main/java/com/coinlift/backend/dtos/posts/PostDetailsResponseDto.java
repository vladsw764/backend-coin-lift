package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record PostDetailsResponseDto(
        UUID uuid,
        String title,
        String content,
        String imageLink,
        LocalDateTime createdAt,
        List<CommentResponseDto> comments
) {
}
