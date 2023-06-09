package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final PostRepository postRepository;

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUser().getId(), false);
    }

    public Comment toCommentEntity(CommentRequestDto commentRequestDto, UUID postId) {
        return Comment.builder()
                .content(commentRequestDto.content())
                .post(postRepository
                        .findById(postId)
                        .orElseThrow(() -> new ResourceNotFoundException("post with id" + postId + "not found")))
                .build();
    }
}