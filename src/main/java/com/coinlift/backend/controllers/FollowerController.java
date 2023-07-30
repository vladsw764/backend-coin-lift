package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.users.FollowerResponseDto;
import com.coinlift.backend.dtos.users.UserMainInfoDto;
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

    /**
     * Endpoint to follow a user by the current user.
     *
     * @param userId The unique ID of the user to follow.
     * @return A response entity with a success message upon successful follow.
     */
    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable String userId) {
        followerService.followUser(UUID.fromString(userId));
        return ResponseEntity.ok("User successfully followed.");
    }

    /**
     * Endpoint to unfollow a user by the current user.
     *
     * @param followingId The unique ID of the user to unfollow.
     * @return A response entity with a success message upon successful unfollow.
     */
    @PostMapping("/{followingId}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable UUID followingId) {
        followerService.unfollowUser(followingId);
        return ResponseEntity.ok("User successfully unfollowed.");
    }

    /**
     * Endpoint to fetch the main information of a user by its ID.
     *
     * @param userId The unique ID of the user to fetch main information.
     * @return A response entity containing the user's main information.
     */
    @GetMapping("/main/{userId}")
    public ResponseEntity<UserMainInfoDto> getUserMainInfo(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(followerService.getUserMainInfo(userId));
    }

    /**
     * Endpoint to fetch the list of followers for a user.
     *
     * @param userId The unique ID of the user to fetch the followers list.
     * @return A response entity containing the list of followers for the user.
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerResponseDto>> getFollowers(@PathVariable UUID userId) {
        List<FollowerResponseDto> followers = followerService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    /**
     * Endpoint to fetch the list of users that a user is following.
     *
     * @param userId The unique ID of the user to fetch the following list.
     * @return A response entity containing the list of users that the user is following.
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowerResponseDto>> getFollowing(@PathVariable UUID userId) {
        List<FollowerResponseDto> following = followerService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    /**
     * Endpoint to get the count of followers for a user.
     *
     * @param userId The unique ID of the user to fetch the follower count.
     * @return A response entity containing the count of followers for the user.
     */
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable UUID userId) {
        int followerCount = followerService.getFollowerCount(userId);
        return ResponseEntity.ok(followerCount);
    }

    /**
     * Endpoint to get the count of users that a user is following.
     *
     * @param userId The unique ID of the user to fetch the following count.
     * @return A response entity containing the count of users that the user is following.
     */
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable UUID userId) {
        int followingCount = followerService.getFollowingCount(userId);
        return ResponseEntity.ok(followingCount);
    }

}
