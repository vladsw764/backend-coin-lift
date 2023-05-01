package com.coinlift.backend.dtos.posts;

public record PostRequestDto(
        String title,
        String content,
        String imageLink
) {
}