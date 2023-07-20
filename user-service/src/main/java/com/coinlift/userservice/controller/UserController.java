package com.coinlift.userservice.controller;

import com.coinlift.userservice.dto.UserResponseDto;
import com.coinlift.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    UserResponseDto getUserByUserId(@PathVariable("id") UUID userId) {
        return userService.getUserById(userId);
    }
}
