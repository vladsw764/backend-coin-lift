package com.coinlift.communityservice.repository;

import com.coinlift.communityservice.model.Comment;
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
}
