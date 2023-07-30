package com.coinlift.backend.services.comments;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.comments.CommentRequestDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    //    List<CommentResponseDto> getCommentsByPostId(UUID postId);
    List<CommentResponseDto> getCommentsByPostId(UUID postId, int page, int size);

    UUID createComment(CommentRequestDto commentRequestDto, UUID postId);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId);

    void deleteComment(UUID postId, UUID commentId);
}
