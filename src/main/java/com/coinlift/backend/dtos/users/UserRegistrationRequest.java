package com.coinlift.backend.dtos.users;

public record UserRegistrationRequest(
        String username,
        String emailAddress,
        String password,
        String confirmPassword
) {
}
