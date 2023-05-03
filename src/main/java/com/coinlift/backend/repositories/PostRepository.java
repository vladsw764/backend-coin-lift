package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.id = ?1")
    Optional<Post> findById(UUID postId);

    @Query(value = "SELECT * FROM posts LIMIT 6", nativeQuery = true)
    List<Post> findLatestPosts();

}
