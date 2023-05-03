package com.coinlift.backend.services.posts;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostResponseDto> getLatestPosts();
    PostDetailsResponseDto getPostById(UUID postId);
    PostResponseDto removePost(UUID postId);
    UUID createPost(PostRequestDto postRequestDto);
    PostResponseDto updatePost(UUID postId);
}
