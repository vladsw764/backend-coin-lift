package com.coinlift.backend.dtos.users;

import java.util.UUID;

public record UserMainInfoDto(

        UUID userId,

        String username,

        String profileImageUrl,

        boolean isFollowing
) {
}
