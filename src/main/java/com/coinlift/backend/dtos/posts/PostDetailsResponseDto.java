package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostDetailsResponseDto {

    private UUID uuid;

    private String username;

    private UUID creatorId;

    private String content;

    private byte[] image;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments;

    private boolean isCreator;

    private boolean isFollowing;

    public PostDetailsResponseDto(UUID uuid, String username, UUID creatorId, String content, byte[] image, LocalDateTime createdAt, List<CommentResponseDto> comments, boolean isCreator, boolean isFollowing) {
        this.uuid = uuid;
        this.username = username;
        this.creatorId = creatorId;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.comments = comments;
        this.isCreator = isCreator;
        this.isFollowing = isFollowing;
    }
}
