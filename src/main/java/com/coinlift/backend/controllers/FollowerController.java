package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.users.FollowerResponseDto;
import com.coinlift.backend.services.followers.FollowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable String userId) {
        followerService.followUser(UUID.fromString(userId));
        return ResponseEntity.ok("User successfully followed.");
    }

    @PostMapping("/{followingId}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable UUID followingId) {
        followerService.unfollowUser(followingId);
        return ResponseEntity.ok("User successfully unfollowed.");
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerResponseDto>> getFollowers(@PathVariable UUID userId) {
        List<FollowerResponseDto> followers = followerService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowerResponseDto>> getFollowing(@PathVariable UUID userId) {
        List<FollowerResponseDto> following = followerService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable UUID userId) {
        int followerCount = followerService.getFollowerCount(userId);
        return ResponseEntity.ok(followerCount);
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable UUID userId) {
        int followingCount = followerService.getFollowingCount(userId);
        return ResponseEntity.ok(followingCount);
    }

}
