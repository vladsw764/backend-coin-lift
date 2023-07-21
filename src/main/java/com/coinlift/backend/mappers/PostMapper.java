package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.dtos.posts.PostShortResponseDto;
import com.coinlift.backend.entities.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public PostDetailsResponseDto toPostDetailsResponseDto(Post post, List<CommentResponseDto> commentResponseDtos) {
        return new PostDetailsResponseDto(post.getId(), post.getUser().getUsername(), post.getUser().getId(), post.getContent(),
                null, post.getCreatedAt(), commentResponseDtos, false, false);
    }

    public PostShortResponseDto toPostShortResponseDto(Post post) {
        return new PostShortResponseDto(post.getId(), post.getContent(), null, post.getComments().size());
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getUser().getUsername(), post.getUser().getId(), post.getContent(), null, post.getComments().size(), post.getCreatedAt(), false);
    }

    public Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .content(postRequestDto.content())
                .build();
    }

}