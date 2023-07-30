package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.users.UserMainInfoDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostDetailsResponseDto(
        UUID uuid,

        String content,

        byte[] image,

        boolean isPostCreator,

        LocalDateTime createdAt,

        Integer commentCount,

        Integer likeCount,

        UserMainInfoDto userMainInfo
) {
}
