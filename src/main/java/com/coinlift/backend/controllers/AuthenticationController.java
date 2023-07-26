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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.register(userRegistrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserAuthenticationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(userRegistrationRequest));
    }
}
