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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void followUser(UUID followingId) {
        UUID followerId = getUserId();
        User from = getUserById(followerId);
        User to = getUserById(followingId);

        if (isFollowing(followingId)) {
            throw new IllegalStateException("You are already following this user.");
        }

        Follower follow = new Follower(from, to);

        from.setFollowingCount(from.getFollowingCount() + 1);
        to.setFollowersCount(to.getFollowersCount() + 1);

        followerRepository.save(follow);
        userRepository.saveAll(List.of(from, to));
    }

    @Override
    @Transactional
    public void unfollowUser(UUID followingId) {
        UUID followerId = getUserId();
        User from = getUserById(followerId);
        User to = getUserById(followingId);

        if (!isFollowing(followingId)) {
            throw new IllegalStateException("You are not following this user.");
        }

        Follower follow = followerRepository.findByFromAndTo(from, to);
        if (follow != null) {
            followerRepository.delete(follow);

            from.setFollowingCount(from.getFollowingCount() - 1);
            to.setFollowersCount(to.getFollowersCount() - 1);

            userRepository.saveAll(List.of(from, to));
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
        return userRepository.findById(userId)
                .map(User::getFollowersCount)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found.", userId)));
    }

    @Override
    public int getFollowingCount(UUID userId) {
        return userRepository.findById(userId)
                .map(User::getFollowingCount)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found.", userId)));
    }

    private boolean isFollowing(UUID to) {
        UUID from = getUserId();
        return followerRepository.existsByFrom_IdAndTo_Id(from, to);
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found.", userId))
        );
    }

    private UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DeniedAccessException("You can't do it before authenticate!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }
}
