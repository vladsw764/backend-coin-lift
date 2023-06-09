package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PostDetailsResponseDto {
    private UUID uuid;
    private String username;
    private String title;
    private String content;
    private byte[] image;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
    private boolean isCreator;
}
