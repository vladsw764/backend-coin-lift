package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
