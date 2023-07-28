package com.coinlift.backend.dtos.posts;

import java.util.UUID;

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

    public PostShortResponseDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
}
