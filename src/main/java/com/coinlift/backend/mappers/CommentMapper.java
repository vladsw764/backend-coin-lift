package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.repositories.PostRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentMapper {

    private final PostRepository postRepository;

    public CommentMapper(PostRepository postRepository) {
        this.postRepository = postRepository;
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