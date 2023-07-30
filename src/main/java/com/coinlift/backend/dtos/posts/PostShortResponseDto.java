package com.coinlift.backend.dtos.posts;

import java.util.UUID;

public record PostShortResponseDto(

        UUID uuid,

        String content,

        byte[] image,

        Integer likeCount
) {
}
