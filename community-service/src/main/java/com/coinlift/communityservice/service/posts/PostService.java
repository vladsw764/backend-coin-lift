package com.coinlift.communityservice.service.posts;

import com.coinlift.communityservice.dto.posts.PostDetailsResponseDto;
import com.coinlift.communityservice.dto.posts.PostRequestDto;
import com.coinlift.communityservice.dto.posts.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostResponseDto> getLatestPosts();

    PostDetailsResponseDto getPostById(UUID postId, Pageable pageable, UUID userId);

    void removePost(UUID postId, UUID userId);

    UUID createPost(PostRequestDto postRequestDto, MultipartFile file, UUID userId);

    PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto, UUID userId);

    List<PostResponseDto> getAllPosts(int page, int size);

    byte[] getPostImage(UUID postId);
}
