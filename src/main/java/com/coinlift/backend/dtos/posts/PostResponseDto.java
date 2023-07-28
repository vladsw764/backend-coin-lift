package com.coinlift.backend.dtos.posts;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public PostResponseDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}