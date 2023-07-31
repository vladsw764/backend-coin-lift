package com.coinlift.backend.dtos.comments;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto(

        UUID id,

        UUID creatorId,

        String content,

        LocalDateTime createdAt,

        boolean isCommentCreator
) {
}
