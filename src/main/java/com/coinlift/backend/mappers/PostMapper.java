package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    public PostDetailsResponseDto toPostDetailsResponseDto(Post post, List<CommentResponseDto> commentResponseDtos) {
        return PostDetailsResponseDto.builder()
                .uuid(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageLink(post.getImageLink())
                .createdAt(post.getCreatedAt())
                .comments(commentResponseDtos)
                .build();
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .uuid(post.getId())
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