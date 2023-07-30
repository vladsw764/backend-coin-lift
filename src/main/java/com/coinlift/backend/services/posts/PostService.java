package com.coinlift.backend.services.posts;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostShortResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<PostShortResponseDto> getLatestPosts();

    PostDetailsResponseDto getPostById(UUID postId, Pageable pageable);

    void removePost(UUID postId);

    UUID createPost(PostRequestDto postRequestDto, MultipartFile file);

    PostDetailsResponseDto updatePost(UUID postId, PostRequestDto postRequestDto);

    List<PostDetailsResponseDto> getAllPosts(int page, int size);

    byte[] getPostImage(UUID postId);
}
