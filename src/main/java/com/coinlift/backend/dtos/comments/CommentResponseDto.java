package com.coinlift.backend.dtos.comments;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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
}