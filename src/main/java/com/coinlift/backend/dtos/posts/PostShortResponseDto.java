package com.coinlift.backend.dtos.posts;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PostShortResponseDto {
    UUID uuid;

    String content;

    byte[] image;

    Integer commentCount;

    public PostShortResponseDto(UUID uuid, String content, byte[] image, Integer commentCount) {
        this.uuid = uuid;
        this.content = content;
        this.image = image;
        this.commentCount = commentCount;
    }
}
