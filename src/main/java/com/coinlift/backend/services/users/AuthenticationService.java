package com.coinlift.backend.services.users;

import com.coinlift.backend.dtos.users.AuthenticationResponse;
import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest);

    AuthenticationResponse authenticate(UserAuthenticationRequest userRegistrationRequest);
}
