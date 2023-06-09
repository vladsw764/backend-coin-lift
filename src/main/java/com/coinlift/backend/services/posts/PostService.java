package com.coinlift.backend.services.posts;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostResponseDto> getLatestPosts();

    PostDetailsResponseDto getPostById(UUID postId, Pageable pageable);

    void removePost(UUID postId);

    UUID createPost(PostRequestDto postRequestDto, MultipartFile file);

    PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto);

    List<PostResponseDto> getAllPosts(int page, int size);

    byte[] getPostImage(UUID postId);
}
