package com.coinlift.backend.dtos.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthenticationRequest {
    private String email;
    private String password;

    public UserAuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
