package com.coinlift.userservice.dto;

public record UserRegistrationRequest(
        String username,
        String emailAddress,
        String password
) {
}
