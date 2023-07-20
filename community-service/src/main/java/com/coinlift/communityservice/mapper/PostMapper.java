package com.coinlift.communityservice.mapper;

import com.coinlift.communityservice.dto.comments.CommentResponseDto;
import com.coinlift.communityservice.dto.posts.PostDetailsResponseDto;
import com.coinlift.communityservice.dto.posts.PostRequestDto;
import com.coinlift.communityservice.dto.posts.PostResponseDto;
import com.coinlift.communityservice.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public PostDetailsResponseDto toPostDetailsResponseDto(Post post, List<CommentResponseDto> commentResponseDtos) {
        return new PostDetailsResponseDto(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getContent(),
                null, post.getCreatedAt(), commentResponseDtos, false);
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getContent(), null, post.getComments().size(), post.getCreatedAt());
    }

    public Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .build();
    }

}