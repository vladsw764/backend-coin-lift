package com.coinlift.communityservice.service.comments;

import com.coinlift.communityservice.dto.comments.CommentRequestDto;
import com.coinlift.communityservice.dto.comments.CommentResponseDto;

import java.util.UUID;

public interface CommentService {
    UUID createComment(CommentRequestDto commentRequestDto, UUID postId, UUID userId);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId, UUID userId);

    void deleteComment(UUID postId, UUID commentId, UUID userId);
}
