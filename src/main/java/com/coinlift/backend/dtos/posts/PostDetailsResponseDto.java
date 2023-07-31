package com.coinlift.backend.dtos.posts;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostDetailsResponseDto(
        UUID uuid,

        UUID creatorId,

        String content,

        byte[] image,

        boolean isPostCreator,

        LocalDateTime createdAt,

        Integer commentCount,

        Integer likeCount
) {
}
