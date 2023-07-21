package com.coinlift.backend.dtos.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PostShortResponseDto {
        UUID uuid;

        String content;

        byte[] image;

        Integer commentCount;
}
