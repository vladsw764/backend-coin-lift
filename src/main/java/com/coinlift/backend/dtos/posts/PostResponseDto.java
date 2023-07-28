package com.coinlift.backend.dtos.posts;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PostResponseDto {

    private UUID uuid;

    private String username;

    private UUID creatorId;

    private String content;

    private byte[] image;

    private Integer commentCount;

    private LocalDateTime createdAt;

    private boolean isFollowing;

    public PostResponseDto(UUID uuid, String username, UUID creatorId, String content, byte[] image, Integer commentCount, LocalDateTime createdAt, boolean isFollowing) {
        this.uuid = uuid;
        this.username = username;
        this.creatorId = creatorId;
        this.content = content;
        this.image = image;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.isFollowing = isFollowing;
    }
}