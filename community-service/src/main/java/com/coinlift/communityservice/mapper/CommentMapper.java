package com.coinlift.communityservice.mapper;

import com.coinlift.communityservice.dto.comments.CommentRequestDto;
import com.coinlift.communityservice.dto.comments.CommentResponseDto;
import com.coinlift.communityservice.model.Comment;
import com.coinlift.communityservice.exception.ResourceNotFoundException;
import com.coinlift.communityservice.repository.PostRepository;
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