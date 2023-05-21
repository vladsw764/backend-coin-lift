package com.coinlift.backend.dtos.users;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationRequest {
    private String email;
    private String password;
}
