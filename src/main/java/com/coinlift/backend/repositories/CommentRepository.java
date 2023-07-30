package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = ?1 ORDER BY c.createdAt DESC")
    List<Comment> findAllByPostId(UUID postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.id = ?1 AND c.id = ?2")
    Optional<Comment> findByPostIdAndCommentId(UUID postId, UUID commentId);

//    @Query("SELECT NEW com.coinlift.backend.dtos.comments.CommentResponseDto(c.id, c.content, c.createdAt, CASE WHEN c.user.id = :currentUserId THEN true ELSE false END) " +
//            "FROM Comment c WHERE c.post.id = :postId " +
//            "ORDER BY (CASE WHEN c.user.id = :currentUserId THEN 1 ELSE 0 END) DESC, c.createdAt ASC")
//    List<CommentResponseDto> getPageOfCommentsByPostId(@Param("postId") UUID postId, @Param("currentUserId") UUID currentUserId, Pageable pageable);
}
