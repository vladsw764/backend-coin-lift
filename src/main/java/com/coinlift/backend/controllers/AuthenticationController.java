package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;
import com.coinlift.backend.pojo.AuthenticationResponse;
import com.coinlift.backend.services.users.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.register(userRegistrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserAuthenticationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(userRegistrationRequest));
    }
}
