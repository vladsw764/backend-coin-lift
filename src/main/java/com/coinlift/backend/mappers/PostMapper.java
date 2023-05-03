package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final CommentMapper commentMapper;
    public PostDetailsResponseDto toPostDetailsResponseDto(Post post) {
        return PostDetailsResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageLink(post.getImageLink())
                .createdAt(post.getCreatedAt())
                .comments(post.getComments().stream().map(commentMapper::toCommentResponseDto).toList())
                .build();
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageLink(post.getImageLink())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .imageLink(postRequestDto.imageLink())
                .build();
    }
}