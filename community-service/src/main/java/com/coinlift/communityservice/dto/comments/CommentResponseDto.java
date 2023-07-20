package com.coinlift.communityservice.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto{
        private UUID id;
        private String content;
        private LocalDateTime createdAt;
        private UUID userId;
        private boolean isCommentCreator;
}