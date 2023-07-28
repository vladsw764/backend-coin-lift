package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("SELECT p FROM Post p WHERE p.imageLink IS NOT NULL ORDER BY p.createdAt DESC LIMIT 6")
    List<Post> findLatestPosts();

}
