package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public PostDetailsResponseDto toPostDetailsResponseDto(Post post, List<CommentResponseDto> commentResponseDtos) {
        return new PostDetailsResponseDto(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getContent(),
                null, post.getCreatedAt(), commentResponseDtos, false);
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getContent(), null, post.getCreatedAt());
    }

    public Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .build();
    }

}