package com.coinlift.backend.services.comments;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.mappers.CommentMapper;
import com.coinlift.backend.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public UUID createComment(CommentRequestDto commentRequestDto, UUID postId) {
        Comment comment = commentRepository.save(commentMapper.toCommentEntity(commentRequestDto, postId));
        return comment.getId();
    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId) {
        Comment comment = getComment(postId, commentId);
        comment.setContent(commentRequestDto.content());
        return commentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(UUID postId, UUID commentId) {
        Comment comment = getComment(postId, commentId);
        commentRepository.deleteById(comment.getId());
    }

    private Comment getComment(UUID postId, UUID commentId) {
        return commentRepository.findByPostIdAndCommentId(postId, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment with id " + commentId + " not found!"));
    }
}
