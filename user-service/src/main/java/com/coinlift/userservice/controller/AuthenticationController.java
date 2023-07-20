package com.coinlift.userservice.controller;

import com.coinlift.userservice.dto.UserAuthenticationRequest;
import com.coinlift.userservice.dto.UserRegistrationRequest;
import com.coinlift.userservice.dto.AuthenticationResponse;
import com.coinlift.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
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
