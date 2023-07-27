package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.users.AuthenticationResponse;
import com.coinlift.backend.dtos.users.UserAuthenticationRequest;
import com.coinlift.backend.dtos.users.UserRegistrationRequest;
import com.coinlift.backend.services.users.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public AuthenticationService authenticationService;

    private UserRegistrationRequest registrationRequest;
    private UserAuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest("username", "email@example.com", "password", "password");
        authenticationRequest = new UserAuthenticationRequest("email@example.com", "password");
    }

    @Test
    void register_ReturnsOkResponseWithToken() throws Exception {
        String token = "test-token";
        AuthenticationResponse response = new AuthenticationResponse(token);

        when(authenticationService.register(registrationRequest)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token)))
                .andDo(print());

        verify(authenticationService, times(1)).register(registrationRequest);
    }

    @Test
    void authenticate_ReturnsOkResponseWithToken() throws Exception {
        String token = "test-token";
        AuthenticationResponse response = new AuthenticationResponse(token);
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token)))
                .andDo(print());

        verify(authenticationService, times(1)).authenticate(authenticationRequest);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}