package com.coinlift.backend.services.comments;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;

import java.util.UUID;

public interface CommentService {
    UUID createComment(CommentRequestDto commentRequestDto, UUID postId, UUID userId);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId, UUID userId);

    void deleteComment(UUID postId, UUID commentId, UUID userId);
}
