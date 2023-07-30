package com.coinlift.backend.services.followers;

import com.coinlift.backend.dtos.users.FollowerResponseDto;
import com.coinlift.backend.dtos.users.UserMainInfoDto;

import java.util.List;
import java.util.UUID;

public interface FollowerService {

    void followUser(UUID followingId);

    void unfollowUser(UUID followingId);

    UserMainInfoDto getUserMainInfo(UUID userId);

    List<FollowerResponseDto> getFollowers(UUID userId);

    List<FollowerResponseDto> getFollowing(UUID userId);

    int getFollowerCount(UUID userId);

    int getFollowingCount(UUID userId);

}
