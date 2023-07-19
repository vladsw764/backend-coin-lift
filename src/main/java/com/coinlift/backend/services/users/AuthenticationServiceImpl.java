package com.coinlift.backend.services.users;

import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;
import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.entities.Role;
import com.coinlift.backend.entities.User;
import com.coinlift.backend.pojo.AuthenticationResponse;
import com.coinlift.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

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
                .build();
        userRepository.save(user);
        MyUserDetails userDetails = new MyUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(UserAuthenticationRequest userRegistrationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword()
        ));

        MyUserDetails userDetails = userDetailsService.loadUserByUsername(userRegistrationRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
