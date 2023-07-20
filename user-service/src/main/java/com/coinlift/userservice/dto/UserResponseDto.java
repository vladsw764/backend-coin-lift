package com.coinlift.userservice.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto (
        UUID id,
        String username,
        String emailAddress
){
}
