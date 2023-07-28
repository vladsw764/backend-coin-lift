package com.coinlift.backend.dtos.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public PostDetailsResponseDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
