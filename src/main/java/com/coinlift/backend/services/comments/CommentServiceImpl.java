package com.coinlift.backend.services.comments;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.exceptions.DeniedAccessException;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.mappers.CommentMapper;
import com.coinlift.backend.repositories.CommentRepository;
import com.coinlift.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public UUID createComment(CommentRequestDto commentRequestDto, UUID postId) {
        UUID userId = getUserId();

        Comment comment = commentMapper.toCommentEntity(commentRequestDto, postId);
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found")));
        return commentRepository.save(comment).getId();
    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId) {
        UUID userId = getUserId();
        Comment comment = getComment(postId, commentId);

        if (isNotCreator(userId, comment)) {
            throw new DeniedAccessException("You don't have access, because you're not creator of this comment!");
        }
        comment.setContent(commentRequestDto.content());
        CommentResponseDto dto = commentMapper.toCommentResponseDto(commentRepository.save(comment));
        dto.setCommentCreator(true);
        return dto;
    }

    @Override
    public void deleteComment(UUID postId, UUID commentId) {
        UUID userId = getUserId();
        Comment comment = getComment(postId, commentId);

        if (isNotCreator(userId, comment)) {
            throw new DeniedAccessException("You don't have access, because you're not creator of this comment!");
        }
        commentRepository.deleteById(comment.getId());
    }

    private static UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DeniedAccessException("You can't do it before authenticate!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }

    private Comment getComment(UUID postId, UUID commentId) {
        return commentRepository.findByPostIdAndCommentId(postId, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment with id " + commentId + " not found!"));
    }

    private static boolean isNotCreator(UUID userId, Comment comment) {
        return !userId.equals(comment.getUser().getId());
    }
}
