package com.coinlift.backend.services.users;

import com.coinlift.backend.dtos.users.AuthenticationResponse;
import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;
import com.coinlift.backend.entities.*;
import com.coinlift.backend.repositories.TokenRepository;
import com.coinlift.backend.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, JwtService jwtService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registers a new user in the system by validating the password and generating a JWT token upon successful registration.
     *
     * @param userRegistrationRequest The user registration request containing user details.
     * @return An AuthenticationResponse containing the generated JWT token upon successful registration.
     * @throws IllegalArgumentException if the password and confirm password do not match during registration.
     */
    @Override
    public AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest) {
        String pass = userRegistrationRequest.password();
        String confirmPass = userRegistrationRequest.confirmPassword();

        if (!pass.equals(confirmPass)) {
            throw new IllegalArgumentException("Password and confirm password don't match");
        }

        User user = User.builder()
                .username(userRegistrationRequest.username())
                .email(userRegistrationRequest.emailAddress())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .role(Role.USER)
                .followersCount(0)
                .followingCount(0)
                .build();
        User savedUser = userRepository.save(user);
        MyUserDetails userDetails = new MyUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);
        revokeUserTokens(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates a user by validating the provided email and password and generates a new JWT token upon successful authentication.
     *
     * @param userRegistrationRequest The user authentication request containing the user's email and password.
     * @return An AuthenticationResponse containing the generated JWT token upon successful authentication.
     */
    @Override
    public AuthenticationResponse authenticate(UserAuthenticationRequest userRegistrationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword()
        ));

        MyUserDetails userDetails = userDetailsService.loadUserByUsername(userRegistrationRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        revokeUserTokens(userDetails.user());
        saveUserToken(userDetails.user(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeUserTokens(User user) {
        List<AuthenticationToken> validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = AuthenticationToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
