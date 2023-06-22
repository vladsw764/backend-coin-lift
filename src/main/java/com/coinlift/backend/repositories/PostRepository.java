package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("select p from Post p where p.imageLink is not null order by p.createdAt desc limit 6")
    List<Post> findLatestPosts();

}
