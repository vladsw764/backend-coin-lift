package com.coinlift.backend.services.followers;

import com.coinlift.backend.dtos.users.FollowerResponseDto;
import com.coinlift.backend.entities.Follower;
import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.entities.User;
import com.coinlift.backend.exceptions.DeniedAccessException;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.repositories.FollowerRepository;
import com.coinlift.backend.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FollowerServiceImpl implements FollowerService {

    private final UserRepository userRepository;

    private final FollowerRepository followerRepository;

    public FollowerServiceImpl(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @Override
    public void followUser(UUID followingId) {
        UUID followerId = getUserId();
        User from = userRepository.findById(followerId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found.", followerId))
        );
        User to = userRepository.findById(followingId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found.", followingId))
        );

        if (isFollowing(followingId)) {
            throw new IllegalStateException("You are already following this user.");
        }

        Follower follow = new Follower(from, to);

        from.setFollowing_count(from.getFollowing_count() + 1);
        to.setFollowers_count(to.getFollowers_count() + 1);

        followerRepository.save(follow);
        userRepository.save(from);
        userRepository.save(to);
    }

    @Override
    public void unfollowUser(UUID followingId) {
        UUID followerId = getUserId();
        User from = userRepository.findById(followerId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found.", followerId))
        );
        User to = userRepository.findById(followingId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found.", followerId))
        );

        if (!isFollowing(followingId)) {
            throw new IllegalStateException("You are not following this user.");
        }

        Follower follow = followerRepository.findByFromAndTo(from, to);
        if (follow != null) {
            followerRepository.delete(follow);
            from.setFollowing_count(from.getFollowing_count() - 1);
            to.setFollowers_count(to.getFollowers_count() - 1);
            userRepository.save(from);
            userRepository.save(to);
        }
    }

    @Override
    public List<FollowerResponseDto> getFollowers(UUID userId) {
        return followerRepository.findAllByToId(userId);
    }

    @Override
    public List<FollowerResponseDto> getFollowing(UUID userId) {
        return followerRepository.findAllByFromId(userId);
    }

    @Override
    public int getFollowerCount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowers_count();
    }

    @Override
    public int getFollowingCount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFollowing_count();
    }

    @Override
    public boolean isFollowing(UUID to) {
        UUID from;
        try {
            from = getUserId();
        } catch (DeniedAccessException e) {
            from = null;
        }
        return followerRepository.existsByFrom_IdAndTo_Id(from, to);
    }

    private static UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DeniedAccessException("You can't do it before authenticate!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }
}
