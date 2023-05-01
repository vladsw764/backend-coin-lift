package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        String title,
        String content,
        String imageLink,
        LocalDateTime createdAt,
        List<CommentResponseDto> comments
) {
}
