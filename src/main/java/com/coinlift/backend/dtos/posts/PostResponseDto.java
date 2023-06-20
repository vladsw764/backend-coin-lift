package com.coinlift.backend.dtos.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private UUID uuid;
    private String username;
    private String title;
    private String content;
    private byte[] image;
    private Integer commentCount;
    private LocalDateTime createdAt;
}