package com.coinlift.userservice.service;

import com.coinlift.userservice.dto.UserAuthenticationRequest;
import com.coinlift.userservice.dto.UserRegistrationRequest;
import com.coinlift.userservice.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest);

    AuthenticationResponse authenticate(UserAuthenticationRequest userRegistrationRequest);
}
