package com.coinlift.backend.dtos.comments;

import com.coinlift.backend.dtos.users.UserMainInfoDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto(

        UUID id,

        String content,

        LocalDateTime createdAt,

        boolean isCommentCreator,

        UserMainInfoDto userMainInfoDto
) {
}
