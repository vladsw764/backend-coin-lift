package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.users.AuthenticationResponse;
import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;
import com.coinlift.backend.services.users.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Register a new user.
     *
     * @param userRegistrationRequest The UserRegistrationRequest object containing user registration details.
     * @return A ResponseEntity containing an AuthenticationResponse object and HttpStatus OK if registration is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.register(userRegistrationRequest));
    }

    /**
     * Authenticate a user based on login credentials.
     *
     * @param userAuthenticationRequest The UserAuthenticationRequest object containing user login credentials.
     * @return A ResponseEntity containing an AuthenticationResponse object and HttpStatus OK if authentication is successful.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(userAuthenticationRequest));
    }
}
