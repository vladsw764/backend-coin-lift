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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves comments for a specific post by its ID with pagination support.
     *
     * @param postId The ID of the post to retrieve comments for.
     * @param page   The page number (0-indexed).
     * @param size   The number of items per page.
     * @return A list of `CommentResponseDto` representing the paginated comments for the post.
     */
    @Override
    public List<CommentResponseDto> getCommentsByPostId(UUID postId, int page, int size) {
        UUID userId = getUserIdOrNull();
        Pageable commentPage = PageRequest.of(page, size);

        List<Comment> comments = commentRepository.findAllByPostId(postId, commentPage);

        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        isCreator(userId, comment)
                )).toList();
    }

    /**
     * Creates a new comment for a specific post.
     *
     * @param commentRequestDto The `CommentRequestDto` containing the details of the new comment.
     * @param postId            The ID of the post for which the comment is created.
     * @return The ID of the newly created comment.
     * @throws ResourceNotFoundException if the user is not found when trying to create the comment.
     */
    @Override
    public UUID createComment(CommentRequestDto commentRequestDto, UUID postId) {
        UUID userId = getUserId();

        Comment comment = commentMapper.toCommentEntity(commentRequestDto, postId);
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found")));
        return commentRepository.save(comment).getId();
    }

    /**
     * Updates a comment for a specific post.
     *
     * @param commentRequestDto The `CommentRequestDto` containing the updated comment details.
     * @param postId            The ID of the post to which the comment belongs.
     * @param commentId         The ID of the comment to update.
     * @return The `CommentResponseDto` representing the updated comment.
     * @throws DeniedAccessException if the user is not the creator of the comment and not authorized to update it.
     */
    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UUID postId, UUID commentId) {
        UUID userId = getUserId();
        Comment comment = getComment(postId, commentId);
        UUID postUserId = comment.getUser().getId();

        checkAccess(userId, comment);

        comment.setContent(commentRequestDto.content());

        commentRepository.save(comment);

        return new CommentResponseDto(
                commentId,
                comment.getUser().getId(),
                commentRequestDto.content(),
                comment.getCreatedAt(),
                isCreator(postUserId, comment)
        );
    }

    /**
     * Deletes a comment for a specific post.
     *
     * @param postId    The ID of the post to which the comment belongs.
     * @param commentId The ID of the comment to delete.
     * @throws DeniedAccessException if the user is not the creator of the comment and not authorized to delete it.
     */
    @Override
    public void deleteComment(UUID postId, UUID commentId) {
        UUID userId = getUserId();
        Comment comment = getComment(postId, commentId);

        checkAccess(userId, comment);

        commentRepository.deleteById(comment.getId());
    }

    private UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DeniedAccessException("You can't do it before authenticate!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }

    private UUID getUserIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }

    private Comment getComment(UUID postId, UUID commentId) {
        return commentRepository.findByPostIdAndCommentId(postId, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment with id " + commentId + " not found!"));
    }

    private void checkAccess(UUID userId, Comment comment) {
        if (!userId.equals(comment.getUser().getId())) {
            throw new DeniedAccessException("You don't have access, because you're not creator of this comment!");
        }
    }

    private static boolean isCreator(UUID userId, Comment comment) {
        if (userId == null) {
            return false;
        }
        return userId.equals(comment.getUser().getId());
    }
}
