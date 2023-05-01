package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .uuid(comment.getUuid())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static Comment toCommentEntity(CommentRequestDto commentRequestDto, Post post) {
        return Comment.builder()
                .content(commentRequestDto.content())
                .post(post)
                .build();
    }
}