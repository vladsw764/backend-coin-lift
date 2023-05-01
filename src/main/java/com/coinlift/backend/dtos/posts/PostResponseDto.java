package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponseDto(
        String title,
        String content,
        String imageLink,
        LocalDateTime createdAt,
        List<CommentResponseDto> comments
) {
}
