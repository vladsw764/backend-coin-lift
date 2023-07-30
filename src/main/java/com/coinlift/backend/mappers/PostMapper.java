package com.coinlift.backend.mappers;

import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toPostEntity(PostRequestDto postRequestDto) {
        return Post.builder()
                .content(postRequestDto.content())
                .build();
    }

}