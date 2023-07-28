package com.coinlift.backend.dtos.comments;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponseDto {

    private UUID id;

    private String content;

    private LocalDateTime createdAt;

    private UUID userId;

    private boolean isCommentCreator;

    public CommentResponseDto(UUID id, String content, LocalDateTime createdAt, UUID userId, boolean isCommentCreator) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.isCommentCreator = isCommentCreator;
    }

    public CommentResponseDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isCommentCreator() {
        return isCommentCreator;
    }

    public void setCommentCreator(boolean commentCreator) {
        isCommentCreator = commentCreator;
    }
}