package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public static PostDetailsResponseDto toPostDetailsResponseDto(Post post) {
        return PostDetailsResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageLink(post.getImageLink())
                .createdAt(post.getCreatedAt())
                .comments(post.getComments().stream().map(CommentMapper::toCommentResponseDto).toList())
                .build();
    }

    public static PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageLink(post.getImageLink())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .imageLink(postRequestDto.imageLink())
                .build();
    }
}