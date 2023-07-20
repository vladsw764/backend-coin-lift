package com.coinlift.userservice.service;

import com.coinlift.userservice.dto.UserResponseDto;
import com.coinlift.userservice.exception.ResourceNotFoundException;
import com.coinlift.userservice.model.User;
import com.coinlift.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponseDto getUserById(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return toDto(user);
    }

    UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmail())
                .build();
    }
}
