package com.coinlift.backend.dtos.users;

import java.util.UUID;

public record FollowerResponseDto(
        String username,

        UUID id
) {
}
